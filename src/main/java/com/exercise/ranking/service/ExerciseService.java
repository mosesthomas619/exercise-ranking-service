package com.exercise.ranking.service;

import com.exercise.ranking.dao.ExerciseRepository;
import com.exercise.ranking.model.Exercise;
import com.exercise.ranking.model.RankingUser;
import com.exercise.ranking.service.exception.ConflictException;
import com.exercise.ranking.service.exception.NotFoundException;
import com.exercise.ranking.service.exception.SecurityException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * Service that coordinates persistence of exercises and user ranking.
 * <p>
 * It is able to save and update exercises. The overlapping exercises per user are not allowed.
 * <p>
 * Exercises that started or finished in the past 28 days count toward the ranking. Order is
 * according to the user's points in descending order. When two users have the same amount of
 * points, the user with the latest done exercise is ranked first in the list.
 * <p>
 * Number of points of the exercise is equal to the burned calories, or the duration of exercise
 * multiplied to the coefficient from the exercise's type if the `calories` is empty.
 * <p>
 * Note: the past 28 days are the days between the beginning of the day that is 28 days in past and
 * the beginning of today. Let's assume that today is 24 June 2021 at 2PM, then the range will be 27
 * May 2021 00:00 until 24 June 2021 00:00.
 */
@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(final ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * Persists the exercise.
     * <p>
     * The methods checks that there is no other exercise already present for the user id, in the
     * period (start + duration) where the new exercise will take place.
     *
     * @param exercise the exercise object.
     * @return the persisted exercise.
     * @throws ConflictException if there is an existing exercise for the exercise's period.
     */
    public Exercise insert(final Exercise exercise) throws ConflictException {
        List<Exercise> exercisesForUserId = exerciseRepository.findByUserId(exercise.getUserId());
        checkConflictExercises(exercisesForUserId, exercise);
        exerciseRepository.save(exercise);
        return exercise;
    }


    /**
     * Saves the exercise for a given id. Not all fields can be updated, only description, startTime,
     * duration, calories.
     * <p>
     * The methods checks that there is no other exercise already present for the user id, in the
     * period (start + duration) where the new exercise will take place.
     *
     * @param exerciseId the id of the exercise to update.
     * @param exercise   the exercise object.
     * @return the save exercise.
     * @throws IllegalArgumentException if the exercise type is different to a persisted one.
     * @throws NotFoundException        if the given exercise id is not present in the database.
     * @throws SecurityException        if the exercise's user is different to a persisted one.
     * @throws ConflictException        if there is an existing exercise for the exercise's period.
     */
    public Exercise update(final Long exerciseId, final Exercise exercise)
            throws IllegalArgumentException, NotFoundException, SecurityException, ConflictException {

        Optional<Exercise> optionalExercise = exerciseRepository.findById(exerciseId);
        if (optionalExercise.isEmpty()) {
            throw new NotFoundException("Exercise with id: " + exerciseId + " not found");
        }
        if (!(optionalExercise.get().getType().equals(exercise.getType()))) {
            throw new IllegalArgumentException("Already existing exercise Type does not match with the input");
        }
        if (!(optionalExercise.get().getUserId().equals(exercise.getUserId()))) {
            throw new SecurityException("Already existing user for this exercise does not match with the input");
        }
        Exercise existingExercise = mapUpdatesToExistingExercise(optionalExercise.get(), exercise);
        List<Exercise> exercisesForUserId = exerciseRepository.findByUserId(existingExercise.getUserId());
        checkConflictExercises(exercisesForUserId, existingExercise);
        exerciseRepository.save(existingExercise);
        return existingExercise;
    }


    /**
     * Calculates ranking for a list of users. Exercises that started or finished in the past 28 days
     * count toward the ranking.
     * <p>
     * The resulting list also contain users who didn't train in the period with 0.0 points and {@link
     * LocalDateTime#MIN} end date and time of the latest user exercise.
     *
     * @param userIds the list of user ids.
     * @return the list of {@link RankingUser} sorted in descending order by points or the end of last
     * exercise if points are the same.
     */
    public List<RankingUser> ranking(final Collection<Long> userIds) {

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        List<Exercise> exerciseList = exerciseRepository.findByUsersAndStartTime((Set<Long>) userIds, startOfToday.minusDays(28));
        List<RankingUser> rankedUserList = new ArrayList<>();
        HashMap<Long, Double> exerciseMap = new HashMap<>();
        HashMap<Long, LocalDateTime> latestExerciseTimes = new HashMap<>();
        double points = 0;
        boolean noCalories;

        for (Exercise data : exerciseList) {
            noCalories = false;
            points = 0;
            if (null == data.getCalories()) {
                noCalories = true;
                points = data.getDuration() * data.getType().getDurationToCaloriesRate();
            }
            if (exerciseMap.containsKey(data.getUserId()) && !noCalories) {
                exerciseMap.put(data.getUserId(), exerciseMap.get(data.getUserId()) + data.getCalories());
            } else if (exerciseMap.containsKey(data.getUserId()) && noCalories) {
                exerciseMap.put(data.getUserId(), exerciseMap.get(data.getUserId()) + points);
            } else if (!exerciseMap.containsKey(data.getUserId()) && !noCalories) {
                exerciseMap.put(data.getUserId(), data.getCalories());
            } else if (!exerciseMap.containsKey(data.getUserId()) && noCalories) {
                exerciseMap.put(data.getUserId(), points);
            }
            if (latestExerciseTimes.containsKey(data.getUserId()) && latestExerciseTimes.get(data.getUserId()).isBefore(data.getStartTime().plusSeconds(data.getDuration()))) {
                latestExerciseTimes.put(data.getUserId(), data.getStartTime().plusSeconds(data.getDuration()));
            } else if (!(latestExerciseTimes.containsKey(data.getUserId()))) {
                latestExerciseTimes.put(data.getUserId(), data.getStartTime().plusSeconds(data.getDuration()));
            }
        }
        for (Map.Entry<Long, Double> m : exerciseMap.entrySet()) {
            rankedUserList.add(new RankingUser(m.getKey(), m.getValue(), latestExerciseTimes.get(m.getKey())));
        }
        List<RankingUser> rankedUsers = rankedUserList.stream().sorted(Comparator.comparing(RankingUser::getPoints, Comparator.reverseOrder()).
                thenComparing(RankingUser::getLatestExerciseTime, Comparator.reverseOrder())).collect(Collectors.toList());

        List<RankingUser> zeroPointUsers = getZeroPointsList(getUsersFromInputIdsWhoHaveNotExercisedInThePast28Days((Set<Long>) userIds, exerciseList));
        rankedUsers.addAll(zeroPointUsers);
        return rankedUsers;
    }


    private void checkConflictExercises(List<Exercise> existingList, Exercise inputExercise) {
        if (!existingList.isEmpty()) {
            LocalDateTime startExisting, endExisting, startNew, endNew;
            for (Exercise ex : existingList) {
                startExisting = ex.getStartTime();
                endExisting = ex.getStartTime().plusSeconds(ex.getDuration());
                startNew = inputExercise.getStartTime();
                endNew = inputExercise.getStartTime().plusSeconds(inputExercise.getDuration());
                if (startExisting.isBefore(endNew) && endExisting.isAfter(startNew)) {            //check for overlap
                    throw new ConflictException("An existing exercise timeline for this user has a conflict with the new exercise timeline");
                }
            }
        }
    }

    private Exercise mapUpdatesToExistingExercise(Exercise existingExercise, Exercise inputExercise) {

        existingExercise.setDescription(inputExercise.getDescription());
        existingExercise.setStartTime(inputExercise.getStartTime());
        existingExercise.setDuration(inputExercise.getDuration());
        if (null != inputExercise.getCalories()) {
            existingExercise.setCalories(inputExercise.getCalories());
        }
        return existingExercise;
    }


    private List<RankingUser> getZeroPointsList(Set<Long> userIds) {

        List<RankingUser> res = new ArrayList<>();
        List<Exercise> resultFromDb = exerciseRepository.findByDistinctUsersAndLatestExerciseTime(userIds);
        for (Exercise exercise : resultFromDb) {
            res.add(new RankingUser(exercise.getUserId(), 0.0, exercise.getStartTime().plusSeconds(exercise.getDuration())));
        }
        return res;
    }

    private Set<Long> getUsersFromInputIdsWhoHaveNotExercisedInThePast28Days(final Set<Long> userIdsFromInput, List<Exercise> exerciseListFromDb) {
        Set<Long> userIdsFromDb = exerciseListFromDb.stream().map(Exercise::getUserId).collect(Collectors.toSet());
        userIdsFromInput.removeAll(userIdsFromDb);
        return userIdsFromInput;
    }
}

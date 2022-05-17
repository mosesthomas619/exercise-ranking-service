package com.exercise.ranking;

import com.exercise.ranking.dao.ExerciseRepository;
import com.exercise.ranking.model.Exercise;
import com.exercise.ranking.model.RankingUser;
import com.exercise.ranking.service.ExerciseService;
import com.exercise.ranking.service.exception.ConflictException;
import com.exercise.ranking.service.exception.NotFoundException;
import com.exercise.ranking.service.exception.SecurityException;
import com.exercise.ranking.testfactory.ExerciseFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;


    @Test
    public void shouldThrowConflictExceptionForOverlappingIntervals() {

        List<Exercise> exercises = ExerciseFactory.getExercises();

        when(exerciseRepository.findByUserId(any())).thenReturn(exercises);
        Exception exception = Assertions.assertThrows(ConflictException.class,
                () -> exerciseService.insert(exercises.get(0)));

        String expectedMessage = "An existing exercise timeline for this user has a conflict with the new exercise timeline";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }



    @Test
    public void shouldThrowNotFoundExceptionIfExerciseIdNotFoundForUpdate() {

        List<Exercise> exercises = ExerciseFactory.getExercises();

        when(exerciseRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> exerciseService.update(4l, exercises.get(0)));

        String expectedMessage = "not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void shouldThrowIllegalArgumentExceptionIfExerciseTypeDoesNotMatchWithUpdateRequest() {

        List<Exercise> exercises = ExerciseFactory.getExercises();

        when(exerciseRepository.findById(any())).thenReturn(Optional.of(exercises.get(0)));
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> exerciseService.update(1l, exercises.get(1)));

        String expectedMessage = "Already existing exercise Type does not match with the input";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }




    @Test
    public void shouldThrowSecurityExceptionIfUserDoesNotMatchWithUpdateRequest() {

        List<Exercise> exercises = ExerciseFactory.getExercises();

        when(exerciseRepository.findById(any())).thenReturn(Optional.of(exercises.get(0)));
        Exception exception = Assertions.assertThrows(SecurityException.class,
                () -> exerciseService.update(1l, exercises.get(2)));

        String expectedMessage = "Already existing user for this exercise does not match with the input";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }



    @Test
    public void shouldReturnRankedUsers() {

        List<Exercise> exercises = ExerciseFactory.getExercises();
        when(exerciseRepository.findByUsersAndStartTime(any(), any())).thenReturn(exercises.subList(0,2));
        when(exerciseRepository.findByDistinctUsersAndLatestExerciseTime(any())).thenReturn(Arrays.asList(exercises.get(2)));

        Set<Long> inputIds = Stream.of(1l,2l,3l)
                .collect(Collectors.toCollection(HashSet::new));

        List<RankingUser> rankList = exerciseService.ranking(inputIds);

        assertEquals(3, rankList.size());

        assertEquals(1000, rankList.get(0).getPoints());
        assertEquals(2, rankList.get(0).getUserId());

        assertEquals(62.400000000000006, rankList.get(1).getPoints());
        assertEquals(1, rankList.get(1).getUserId());

        assertEquals(0.0, rankList.get(2).getPoints());
        assertEquals(3, rankList.get(2).getUserId());
    }

}

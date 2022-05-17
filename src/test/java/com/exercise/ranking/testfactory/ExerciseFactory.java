package com.exercise.ranking.testfactory;

import com.exercise.ranking.facade.dto.ExerciseDTO;
import com.exercise.ranking.model.Exercise;
import com.exercise.ranking.model.ExerciseType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExerciseFactory {

    public static List<Exercise> getExercises() {
        List<Exercise> exerciseList = new ArrayList<>();

        Exercise e1 = new Exercise();
        e1.setDescription("des1");
        e1.setDuration(60L);
        e1.setUserId(1L);
        e1.setStartTime(LocalDateTime.parse("2022-04-13T17:09:42.411"));
        e1.setType(ExerciseType.RUNNING);


        Exercise e2 = new Exercise();
        e2.setDescription("des2");
        e2.setDuration(60L);
        e2.setUserId(2L);
        e2.setStartTime(LocalDateTime.parse("2022-04-15T17:09:42.411"));
        e2.setType(ExerciseType.CIRCUIT_TRAINING);
        e2.setCalories(1000.0);

        Exercise e3 = new Exercise();
        e3.setDescription("des3");
        e3.setDuration(60L);
        e3.setUserId(3L);
        e3.setStartTime(LocalDateTime.parse("2022-01-14T17:09:42.411"));
        e3.setType(ExerciseType.RUNNING);

        exerciseList.add(e1);
        exerciseList.add(e2);
        exerciseList.add(e3);

        return exerciseList;

    }


    public static List<ExerciseDTO> getExerciseDTO() {
        List<ExerciseDTO> exerciseList = new ArrayList<>();

        ExerciseDTO e1 = new ExerciseDTO();
        e1.setDescription("des1");
        e1.setDuration(60L);
        e1.setUserId(1L);
        e1.setStartTime(OffsetDateTime.parse("2022-04-13T17:09:46.710576+02"));
        e1.setType(ExerciseType.RUNNING);


        ExerciseDTO e2 = new ExerciseDTO();
        e2.setDescription("des2");
        e2.setDuration(60L);
        e2.setUserId(2L);
        e2.setStartTime(OffsetDateTime.parse("2022-04-15T17:09:46.710576+02"));
        e2.setType(ExerciseType.RUNNING);

        ExerciseDTO e3 = new ExerciseDTO();
        e3.setDescription("des3");
        e3.setDuration(60L);
        e3.setUserId(1L);
        e3.setStartTime(OffsetDateTime.parse("2022-04-14T17:09:46.710576+02"));
        e3.setType(ExerciseType.RUNNING);

        exerciseList.add(e1);
        exerciseList.add(e2);
        exerciseList.add(e3);

        return exerciseList;

    }

}

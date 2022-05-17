package com.exercise.ranking.dao;

import com.exercise.ranking.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByUserId(Long userId);


    @Query(value = "SELECT * FROM exercises e WHERE e.user_id IN (:userIds) AND e.start_time >= :localDateTime", nativeQuery = true)
    List<Exercise> findByUsersAndStartTime(Set<Long> userIds, LocalDateTime localDateTime);


    @Query(value = "SELECT DISTINCT ON (user_id) * FROM exercises e WHERE e.user_id IN (:userIds) ORDER BY user_id, start_time DESC", nativeQuery = true)
    List<Exercise> findByDistinctUsersAndLatestExerciseTime(Set<Long> userIds);


}



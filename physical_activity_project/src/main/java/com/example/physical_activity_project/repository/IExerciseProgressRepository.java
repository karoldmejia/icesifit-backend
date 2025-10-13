package com.example.physical_activity_project.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.physical_activity_project.model.ExerciseProgress;

public interface IExerciseProgressRepository extends JpaRepository<ExerciseProgress, Long> {

    List<ExerciseProgress> findByUser_Id(Long userId);

    @Query("SELECT ep FROM ExerciseProgress ep WHERE ep.routineExercise.routine.id = :routineId")
    List<ExerciseProgress> findByRoutine_Id(@Param("routineId") Long routineId);

    @Query("SELECT ep FROM ExerciseProgress ep WHERE ep.user.id = :userId AND ep.progressDate BETWEEN :startDate AND :endDate")
    List<ExerciseProgress> findByUserAndDateRange(@Param("userId") Long userId,
                                                  @Param("startDate") Timestamp startDate,
                                                  @Param("endDate") Timestamp endDate);
}

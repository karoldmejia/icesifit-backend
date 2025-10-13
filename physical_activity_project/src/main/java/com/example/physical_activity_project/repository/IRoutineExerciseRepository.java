package com.example.physical_activity_project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.physical_activity_project.model.RoutineExercise;

public interface IRoutineExerciseRepository extends JpaRepository<RoutineExercise, Long> {

    List<RoutineExercise> findByRoutine_Id(Long routineId);

    List<RoutineExercise> findByExercise_Id(Long exerciseId);
}

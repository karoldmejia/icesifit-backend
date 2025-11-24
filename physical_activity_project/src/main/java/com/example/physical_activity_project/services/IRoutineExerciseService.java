package com.example.physical_activity_project.services;

import java.util.List;

import com.example.physical_activity_project.dto.RoutineExerciseDTO;
import com.example.physical_activity_project.model.RoutineExercise;

public interface IRoutineExerciseService {

    RoutineExercise createRoutineExercise(RoutineExerciseDTO dto);

    RoutineExercise updateRoutineExercise(Long id, RoutineExerciseDTO dto);

    void deleteRoutineExercise(Long id);

    List<RoutineExercise> getRoutineExercisesByRoutine(Long routineId);

    List<RoutineExercise> getRoutineExercisesByExercise(Long exerciseId);
    List<RoutineExercise> getRoutineExercisesByUserRoutine(Long userRoutineId);

    List<RoutineExercise> getAllRoutineExercises();
}

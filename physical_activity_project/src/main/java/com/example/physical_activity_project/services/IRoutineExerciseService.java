package com.example.physical_activity_project.services;

import java.util.List;

import com.example.physical_activity_project.model.RoutineExercise;

public interface IRoutineExerciseService {

    RoutineExercise createRoutineExercise(RoutineExercise routineExercise);

    RoutineExercise updateRoutineExercise(Long id, RoutineExercise updatedRoutineExercise);

    void deleteRoutineExercise(Long id);

    List<RoutineExercise> getRoutineExercisesByRoutine(Long routineId);

    List<RoutineExercise> getRoutineExercisesByExercise(Long exerciseId);

    List<RoutineExercise> getAllRoutineExercises();
}

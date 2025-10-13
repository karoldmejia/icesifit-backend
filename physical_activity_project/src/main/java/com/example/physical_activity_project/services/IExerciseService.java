package com.example.physical_activity_project.services;

import java.util.List;
import com.example.physical_activity_project.model.Exercise;

public interface IExerciseService {
    Exercise createExercise(Exercise exercise);
    Exercise updateExercise(Long id, Exercise updatedExercise);
    void deleteExercise(Long id);
    Exercise getExerciseById(Long id);
    List<Exercise> getAllExercises();
    List<Exercise> getExercisesByType(String type);
    List<Exercise> getExercisesByDifficulty(String difficulty);
}

package com.example.physical_activity_project.services;

import java.time.LocalDate;
import java.util.List;

import com.example.physical_activity_project.dto.ProgressDTO;
import com.example.physical_activity_project.model.ExerciseProgress;

public interface IExerciseProgressService {

    ExerciseProgress registerProgress(Long userId, ExerciseProgress progress);

    ExerciseProgress updateProgress(Long progressId, ExerciseProgress updated);

    void deleteProgress(Long progressId);

    List<ExerciseProgress> getProgressByUser(Long userId);

    List<ExerciseProgress> getProgressByRoutine(Long routineId);

    List<ExerciseProgress> getProgressByWeek(Long userId, LocalDate startDate);

    ProgressDTO getProgressSummary(Long userId, LocalDate startDate, LocalDate endDate);

    List<ExerciseProgress> getAllProgress();
}

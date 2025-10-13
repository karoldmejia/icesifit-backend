package com.example.physical_activity_project.services.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.dto.ProgressDTO;
import com.example.physical_activity_project.repository.IExerciseProgressRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.IExerciseProgressService;

@Service
public class ExerciseProgressServiceImpl implements IExerciseProgressService {

    @Autowired
    private IExerciseProgressRepository exerciseProgressRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public ExerciseProgress registerProgress(Long userId, ExerciseProgress progress) {
        progress.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)));
        return exerciseProgressRepository.save(progress);
    }

    @Override
    public ExerciseProgress updateProgress(Long progressId, ExerciseProgress updated) {
        Optional<ExerciseProgress> optional = exerciseProgressRepository.findById(progressId);

        if (optional.isPresent()) {
            ExerciseProgress existing = optional.get();
            existing.setProgressDate(updated.getProgressDate());
            existing.setSetsCompleted(updated.getSetsCompleted());
            existing.setRepsCompleted(updated.getRepsCompleted());
            existing.setTimeCompleted(updated.getTimeCompleted());
            existing.setEffortLevel(updated.getEffortLevel());
            existing.setRoutineExercise(updated.getRoutineExercise());
            return exerciseProgressRepository.save(existing);
        } else {
            throw new RuntimeException("Progress not found with id: " + progressId);
        }
    }

    @Override
    public void deleteProgress(Long progressId) {
        if (exerciseProgressRepository.existsById(progressId)) {
            exerciseProgressRepository.deleteById(progressId);
        } else {
            throw new RuntimeException("Cannot delete. Progress not found with id: " + progressId);
        }
    }

    @Override
    public List<ExerciseProgress> getProgressByUser(Long userId) {
        return exerciseProgressRepository.findByUser_Id(userId);
    }

    @Override
    public List<ExerciseProgress> getProgressByRoutine(Long routineId) {
        return exerciseProgressRepository.findByRoutine_Id(routineId);
    }

    @Override
    public List<ExerciseProgress> getProgressByWeek(Long userId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        Timestamp start = Timestamp.valueOf(LocalDateTime.of(startDate, java.time.LocalTime.MIN));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(endDate, java.time.LocalTime.MAX));
        return exerciseProgressRepository.findByUserAndDateRange(userId, start, end);
    }

    @Override
    public ProgressDTO getProgressSummary(Long userId, LocalDate start, LocalDate end) {
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.of(start, java.time.LocalTime.MIN));
        Timestamp endDate = Timestamp.valueOf(LocalDateTime.of(end, java.time.LocalTime.MAX));

        List<ExerciseProgress> progresses = exerciseProgressRepository.findByUserAndDateRange(userId, startDate, endDate);

        if (progresses.isEmpty()) {
            return new ProgressDTO(0L, 0, 0, 0.0);
        }

        long totalExercises = progresses.size();
        int totalReps = progresses.stream().mapToInt(p -> p.getRepsCompleted() != null ? p.getRepsCompleted() : 0).sum();
        int totalTime = progresses.stream().mapToInt(p -> p.getTimeCompleted() != null ? p.getTimeCompleted() : 0).sum();
        double avgEffort = progresses.stream().mapToInt(p -> p.getEffortLevel() != null ? p.getEffortLevel() : 0).average().orElse(0.0);

        return new ProgressDTO(totalExercises, totalReps, totalTime, avgEffort);
    }

    @Override
    public List<ExerciseProgress> getAllProgress() {
        return exerciseProgressRepository.findAll();
    }
}

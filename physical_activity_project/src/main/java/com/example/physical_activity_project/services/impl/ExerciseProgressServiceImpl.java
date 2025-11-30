package com.example.physical_activity_project.services.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Comparator;

import com.example.physical_activity_project.dto.UserCountByDateDTO;
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
        return getProgressInRange(userId, startDate, endDate);
    }

    @Override
    public ProgressDTO getProgressSummary(Long userId, LocalDate start, LocalDate end) {
        return calculateProgressSummary(userId, start, end);
    }

    @Override
    public List<ExerciseProgress> getProgressByRoutineExercise(Long routineExerciseId) {
        return exerciseProgressRepository.findByRoutineExercise_IdOrderByProgressDateDesc(routineExerciseId);
    }


    @Override
    public List<ExerciseProgress> getAllProgress() {
        return exerciseProgressRepository.findAll();
    }

    private List<ExerciseProgress> getProgressInRange(Long userId, LocalDate start, LocalDate end) {
        Timestamp startTs = Timestamp.valueOf(LocalDateTime.of(start, java.time.LocalTime.MIN));
        Timestamp endTs = Timestamp.valueOf(LocalDateTime.of(end, java.time.LocalTime.MAX));
        return exerciseProgressRepository.findByUserAndDateRange(userId, startTs, endTs);
    }

    private ProgressDTO calculateProgressSummary(Long userId, LocalDate start, LocalDate end) {
        List<ExerciseProgress> progresses = getProgressInRange(userId, start, end);

        long totalExercises = progresses.size();
        int totalSets = progresses.stream()
                .mapToInt(p -> p.getSetsCompleted() != null ? p.getSetsCompleted() : 0)
                .sum();
        int totalReps = progresses.stream()
                .mapToInt(p -> p.getRepsCompleted() != null ? p.getRepsCompleted() : 0)
                .sum();
        int totalTime = progresses.stream()
                .mapToInt(p -> p.getTimeCompleted() != null ? p.getTimeCompleted() : 0)
                .sum();
        double avgEffort = progresses.stream()
                .mapToInt(p -> p.getEffortLevel() != null ? p.getEffortLevel() : 0)
                .average()
                .orElse(0.0);

        return new ProgressDTO(totalExercises, totalSets, totalReps, totalTime, avgEffort);
    }

    @Override
    public List<ExerciseProgress> getProgressByRoutineAndWeek(Long routineId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        Timestamp startTs = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTs = Timestamp.valueOf(endDate.atTime(23, 59, 59));
        return exerciseProgressRepository.findByRoutineExercise_UserRoutine_IdAndProgressDateBetween(routineId, startTs, endTs);
    }

    @Override
    public List<UserCountByDateDTO> getUsersCountByRoutineByDate(Long routineId) {

        // Traemos todos los ExerciseProgress de la rutina
        List<ExerciseProgress> progresses = exerciseProgressRepository.findByRoutineOrUserRoutineRoutineId(routineId);
        progresses.forEach(ep -> System.out.println(
                "ProgressId: " + ep.getId() +
                        ", UserId: " + ep.getUser().getId() +
                        ", ProgressDate: " + ep.getProgressDate()
        ));

        // Agrupamos por fecha (solo la parte de fecha, ignorando hora)
        Map<LocalDate, Set<Long>> usersPerDay = progresses.stream()
                .collect(Collectors.groupingBy(
                        ep -> ep.getProgressDate().toLocalDateTime().toLocalDate(), // convierte Timestamp -> LocalDate
                        Collectors.mapping(ep -> ep.getUser().getId(), Collectors.toSet()) // obtenemos IDs únicos de usuario
                ));

        usersPerDay.forEach((date, users) -> System.out.println("Fecha: " + date + ", UserIds: " + users));

        // Convertimos el Map en lista de DTOs
        List<UserCountByDateDTO> result = usersPerDay.entrySet().stream()
                .map(entry -> new UserCountByDateDTO(
                        Date.valueOf(entry.getKey()), // java.sql.Date
                        (long) entry.getValue().size() // cantidad de usuarios distintos
                ))
                .sorted(Comparator.comparing(UserCountByDateDTO::getDate)) // opcional: ordenar por fecha
                .toList();

        result.forEach(dto -> System.out.println("Date: " + dto.getDate() + ", UserCount: " + dto.getUserCount()));

        return result;
    }

}


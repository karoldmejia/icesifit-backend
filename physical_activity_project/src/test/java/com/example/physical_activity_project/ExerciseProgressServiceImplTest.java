package com.example.physical_activity_project;

import com.example.physical_activity_project.dto.ProgressDTO;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.IExerciseProgressRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.impl.ExerciseProgressServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ExerciseProgressServiceImplTest {

    @Mock
    private IExerciseProgressRepository exerciseProgressRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private ExerciseProgressServiceImpl exerciseProgressService;

    private ExerciseProgress progress;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Heiner");

        progress = new ExerciseProgress();
        progress.setId(10L);
        progress.setProgressDate(Timestamp.valueOf(LocalDateTime.of(2025, 2, 10, 10, 0)));
        progress.setSetsCompleted(3);
        progress.setRepsCompleted(30);
        progress.setTimeCompleted(60);
        progress.setEffortLevel(7);
        progress.setUser(user);
    }


    @Test
    void testRegisterProgress_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(exerciseProgressRepository.save(any(ExerciseProgress.class))).thenReturn(progress);

        ExerciseProgress saved = exerciseProgressService.registerProgress(1L, progress);

        assertNotNull(saved);
        assertEquals(1L, saved.getUser().getId());
        verify(userRepository).findById(1L);
        verify(exerciseProgressRepository).save(progress);
    }

    @Test
    void testRegisterProgress_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> exerciseProgressService.registerProgress(1L, progress));

        assertEquals("User not found with id: 1", ex.getMessage());
    }


    @Test
    void testUpdateProgress_Success() {
        ExerciseProgress updated = new ExerciseProgress();
        updated.setProgressDate(Timestamp.valueOf(LocalDateTime.of(2025, 3, 5, 9, 0)));
        updated.setSetsCompleted(4);
        updated.setRepsCompleted(40);
        updated.setTimeCompleted(80);
        updated.setEffortLevel(8);

        when(exerciseProgressRepository.findById(10L)).thenReturn(Optional.of(progress));
        when(exerciseProgressRepository.save(any(ExerciseProgress.class))).thenReturn(updated);

        ExerciseProgress result = exerciseProgressService.updateProgress(10L, updated);

        assertEquals(4, result.getSetsCompleted());
        assertEquals(8, result.getEffortLevel());
        verify(exerciseProgressRepository).findById(10L);
        verify(exerciseProgressRepository).save(any(ExerciseProgress.class));
    }

    @Test
    void testUpdateProgress_NotFound() {
        when(exerciseProgressRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> exerciseProgressService.updateProgress(10L, progress));

        assertEquals("Progress not found with id: 10", ex.getMessage());
    }


    @Test
    void testDeleteProgress_Success() {
        when(exerciseProgressRepository.existsById(10L)).thenReturn(true);
        doNothing().when(exerciseProgressRepository).deleteById(10L);

        exerciseProgressService.deleteProgress(10L);

        verify(exerciseProgressRepository).deleteById(10L);
    }

    @Test
    void testDeleteProgress_NotFound() {
        when(exerciseProgressRepository.existsById(10L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> exerciseProgressService.deleteProgress(10L));

        assertEquals("Cannot delete. Progress not found with id: 10", ex.getMessage());
    }


    @Test
    void testGetProgressByUser() {
        when(exerciseProgressRepository.findByUser_Id(1L)).thenReturn(List.of(progress));

        List<ExerciseProgress> result = exerciseProgressService.getProgressByUser(1L);

        assertEquals(1, result.size());
        verify(exerciseProgressRepository).findByUser_Id(1L);
    }


    @Test
    void testGetProgressByRoutine() {
        when(exerciseProgressRepository.findByRoutine_Id(2L)).thenReturn(List.of(progress));

        List<ExerciseProgress> result = exerciseProgressService.getProgressByRoutine(2L);

        assertEquals(1, result.size());
        verify(exerciseProgressRepository).findByRoutine_Id(2L);
    }


    @Test
    void testGetProgressByWeek() {
        LocalDate start = LocalDate.of(2025, 2, 3);
        Timestamp startTs = Timestamp.valueOf(LocalDateTime.of(start, java.time.LocalTime.MIN));
        Timestamp endTs = Timestamp.valueOf(LocalDateTime.of(start.plusDays(6), java.time.LocalTime.MAX));

        when(exerciseProgressRepository.findByUserAndDateRange(1L, startTs, endTs))
                .thenReturn(List.of(progress));

        List<ExerciseProgress> result = exerciseProgressService.getProgressByWeek(1L, start);

        assertEquals(1, result.size());
        verify(exerciseProgressRepository).findByUserAndDateRange(1L, startTs, endTs);
    }


    @Test
    void testGetProgressSummary_WithData() {
        LocalDate start = LocalDate.of(2025, 2, 1);
        LocalDate end = LocalDate.of(2025, 2, 10);

        when(exerciseProgressRepository.findByUserAndDateRange(anyLong(), any(), any()))
                .thenReturn(List.of(progress));

        ProgressDTO summary = exerciseProgressService.getProgressSummary(1L, start, end);

        assertEquals(1L, summary.getTotalExercises());
        assertEquals(30, summary.getTotalReps());
        assertEquals(60, summary.getTotalTime());
        assertEquals(7.0, summary.getAverageEffort());
    }

    @Test
    void testGetProgressSummary_Empty() {
        when(exerciseProgressRepository.findByUserAndDateRange(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        ProgressDTO summary = exerciseProgressService.getProgressSummary(1L, LocalDate.now(), LocalDate.now());

        assertEquals(0, summary.getTotalExercises());
        assertEquals(0, summary.getTotalReps());
        assertEquals(0, summary.getTotalTime());
        assertEquals(0.0, summary.getAverageEffort());
    }


    @Test
    void testGetAllProgress() {
        when(exerciseProgressRepository.findAll()).thenReturn(List.of(progress));

        List<ExerciseProgress> result = exerciseProgressService.getAllProgress();

        assertEquals(1, result.size());
        verify(exerciseProgressRepository).findAll();
    }
    @Test
    void testGetProgressSummary_WithNullValues() {
        LocalDate start = LocalDate.of(2025, 2, 1);
        LocalDate end = LocalDate.of(2025, 2, 10);


        ExerciseProgress progressNulls = new ExerciseProgress();
        progressNulls.setRepsCompleted(null);
        progressNulls.setTimeCompleted(null);
        progressNulls.setEffortLevel(null);

        when(exerciseProgressRepository.findByUserAndDateRange(anyLong(), any(), any()))
                .thenReturn(List.of(progressNulls));

        ProgressDTO summary = exerciseProgressService.getProgressSummary(1L, start, end);


        assertEquals(1L, summary.getTotalExercises());
        assertEquals(0, summary.getTotalReps());
        assertEquals(0, summary.getTotalTime());
        assertEquals(0.0, summary.getAverageEffort());

        verify(exerciseProgressRepository).findByUserAndDateRange(anyLong(), any(), any());
    }

}
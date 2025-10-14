package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Exercise;
import com.example.physical_activity_project.repository.IExerciseRepository;
import com.example.physical_activity_project.services.impl.ExerciseServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExerciseServiceImplTest {

    @Mock
    private IExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    private Exercise exercise;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exercise = new Exercise();
        exercise.setId(1L);
        exercise.setName("Push Up");
        exercise.setType("Strength");
        exercise.setDescription("Basic push up");
        exercise.setDuration(30D);
        exercise.setDifficulty("Medium");
        exercise.setVideoUrl("https://video.com/pushup");
    }

    @Test
    void testCreateExercise() {
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        Exercise created = exerciseService.createExercise(exercise);

        assertNotNull(created);
        assertEquals("Push Up", created.getName());
        verify(exerciseRepository, times(1)).save(exercise);
    }

    @Test
    void testUpdateExercise_Success() {
        Exercise updateData = new Exercise();
        updateData.setName("Modified Push Up");
        updateData.setType("Strength");
        updateData.setDescription("Modified version");
        updateData.setDuration(45D);
        updateData.setDifficulty("Hard");
        updateData.setVideoUrl("https://video.com/pushup-modified");

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(updateData);

        Exercise updated = exerciseService.updateExercise(1L, updateData);

        assertEquals("Modified Push Up", updated.getName());
        verify(exerciseRepository).findById(1L);
        verify(exerciseRepository).save(any(Exercise.class));
    }

    @Test
    void testUpdateExercise_NotFound() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> exerciseService.updateExercise(1L, exercise));
        assertEquals("Exercise not found with id: 1", ex.getMessage());
    }

    @Test
    void testDeleteExercise_Success() {
        when(exerciseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(exerciseRepository).deleteById(1L);

        exerciseService.deleteExercise(1L);

        verify(exerciseRepository).deleteById(1L);
    }

    @Test
    void testDeleteExercise_NotFound() {
        when(exerciseRepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> exerciseService.deleteExercise(1L));

        assertEquals("Cannot delete. Exercise not found with id: 1", ex.getMessage());
    }

    @Test
    void testGetExerciseById_Success() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));

        Exercise found = exerciseService.getExerciseById(1L);

        assertEquals("Push Up", found.getName());
        verify(exerciseRepository).findById(1L);
    }

    @Test
    void testGetExerciseById_NotFound() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> exerciseService.getExerciseById(1L));

        assertEquals("Exercise not found with id: 1", ex.getMessage());
    }

    @Test
    void testGetAllExercises() {
        when(exerciseRepository.findAll()).thenReturn(List.of(exercise));

        List<Exercise> result = exerciseService.getAllExercises();

        assertEquals(1, result.size());
        verify(exerciseRepository).findAll();
    }

    @Test
    void testGetExercisesByType() {
        when(exerciseRepository.findByType("Strength")).thenReturn(List.of(exercise));

        List<Exercise> result = exerciseService.getExercisesByType("Strength");

        assertEquals(1, result.size());
        verify(exerciseRepository).findByType("Strength");
    }

    @Test
    void testGetExercisesByDifficulty() {
        when(exerciseRepository.findByDifficulty("Medium")).thenReturn(List.of(exercise));

        List<Exercise> result = exerciseService.getExercisesByDifficulty("Medium");

        assertEquals(1, result.size());
        verify(exerciseRepository).findByDifficulty("Medium");
    }
}

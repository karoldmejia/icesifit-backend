package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Exercise;
import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.model.RoutineExercise;
import com.example.physical_activity_project.repository.IRoutineExerciseRepository;
import com.example.physical_activity_project.services.impl.RoutineExerciseServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoutineExerciseServiceImplTest {

    @Mock
    private IRoutineExerciseRepository routineExerciseRepository;

    @InjectMocks
    private RoutineExerciseServiceImpl routineExerciseService;

    private RoutineExercise routineExercise;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Routine routine = new Routine();
        routine.setId(1L);
        routine.setName("Rutina fuerza");

        Exercise exercise = new Exercise();
        exercise.setId(1L);
        exercise.setName("Press banca");

        routineExercise = new RoutineExercise();
        routineExercise.setId(1L);
        routineExercise.setSets(4);
        routineExercise.setReps(10);
        routineExercise.setTime(0);
        routineExercise.setExercise(exercise);
        routineExercise.setRoutine(routine);
    }


    @Test
    void createRoutineExercise_returnsSaved() {
        when(routineExerciseRepository.save(routineExercise)).thenReturn(routineExercise);

        RoutineExercise result = routineExerciseService.createRoutineExercise(routineExercise);

        assertNotNull(result);
        assertEquals(4, result.getSets());
        assertEquals(10, result.getReps());
        verify(routineExerciseRepository).save(routineExercise);
    }


    @Test
    void updateRoutineExercise_existing_updatesAndReturns() {
        RoutineExercise updated = new RoutineExercise();
        updated.setSets(5);
        updated.setReps(12);
        updated.setTime(60);

        Routine newRoutine = new Routine();
        newRoutine.setId(2L);
        updated.setRoutine(newRoutine);

        Exercise newExercise = new Exercise();
        newExercise.setId(3L);
        updated.setExercise(newExercise);

        when(routineExerciseRepository.findById(1L)).thenReturn(Optional.of(routineExercise));
        when(routineExerciseRepository.save(any(RoutineExercise.class))).thenAnswer(i -> i.getArgument(0));

        RoutineExercise result = routineExerciseService.updateRoutineExercise(1L, updated);

        assertEquals(5, result.getSets());
        assertEquals(12, result.getReps());
        assertEquals(60, result.getTime());
        assertEquals(2L, result.getRoutine().getId());
        assertEquals(3L, result.getExercise().getId());
        verify(routineExerciseRepository).save(routineExercise);
    }

    @Test
    void updateRoutineExercise_notFound_throwsException() {
        when(routineExerciseRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                routineExerciseService.updateRoutineExercise(99L, routineExercise));

        assertEquals("RoutineExercise not found with id: 99", ex.getMessage());
        verify(routineExerciseRepository, never()).save(any());
    }


    @Test
    void deleteRoutineExercise_existing_deletesSuccessfully() {
        when(routineExerciseRepository.existsById(1L)).thenReturn(true);

        routineExerciseService.deleteRoutineExercise(1L);

        verify(routineExerciseRepository).deleteById(1L);
    }

    @Test
    void deleteRoutineExercise_notFound_throwsException() {
        when(routineExerciseRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                routineExerciseService.deleteRoutineExercise(99L));

        assertEquals("Cannot delete. RoutineExercise not found with id: 99", ex.getMessage());
        verify(routineExerciseRepository, never()).deleteById(any());
    }

    @Test
    void getRoutineExercisesByRoutine_returnsList() {
        List<RoutineExercise> list = Arrays.asList(routineExercise);
        when(routineExerciseRepository.findByRoutine_Id(1L)).thenReturn(list);

        List<RoutineExercise> result = routineExerciseService.getRoutineExercisesByRoutine(1L);

        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getSets());
        verify(routineExerciseRepository).findByRoutine_Id(1L);
    }


    @Test
    void getRoutineExercisesByExercise_returnsList() {
        List<RoutineExercise> list = Arrays.asList(routineExercise);
        when(routineExerciseRepository.findByExercise_Id(1L)).thenReturn(list);

        List<RoutineExercise> result = routineExerciseService.getRoutineExercisesByExercise(1L);

        assertEquals(1, result.size());
        assertEquals("Press banca", result.get(0).getExercise().getName());
        verify(routineExerciseRepository).findByExercise_Id(1L);
    }


    @Test
    void getAllRoutineExercises_returnsAll() {
        when(routineExerciseRepository.findAll()).thenReturn(List.of(routineExercise));

        List<RoutineExercise> result = routineExerciseService.getAllRoutineExercises();

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getReps());
        verify(routineExerciseRepository).findAll();
    }
}
package com.example.physical_activity_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.repository.IRoutineRepository;
import com.example.physical_activity_project.services.impl.RoutineServiceImpl;

class RoutineServiceImplTest {

    @Mock
    private IRoutineRepository routineRepository;

    @InjectMocks
    private RoutineServiceImpl routineService;

    private Routine routine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        routine = new Routine();
        routine.setId(1L);
        routine.setName("Rutina de fuerza");
        routine.setCertified(true);
        routine.setCreationDate(Timestamp.valueOf("2025-01-01 00:00:00"));
        routine.setRoutineExercises(List.of());
    }


    @Test
    void createRoutine_returnsSavedRoutine() {
        when(routineRepository.save(routine)).thenReturn(routine);

        Routine result = routineService.createRoutine(routine);

        assertNotNull(result);
        assertEquals("Rutina de fuerza", result.getName());
        verify(routineRepository).save(routine);
    }


    @Test
    void updateRoutine_existingRoutine_updatesAndReturns() {
        Routine updated = new Routine();
        updated.setName("Rutina avanzada");
        updated.setCertified(false);
        updated.setCreationDate(Timestamp.valueOf("2025-03-03 00:00:00"));
        updated.setRoutineExercises(List.of());

        when(routineRepository.findById(1L)).thenReturn(Optional.of(routine));
        when(routineRepository.save(any(Routine.class))).thenAnswer(i -> i.getArgument(0));

        Routine result = routineService.updateRoutine(1L, updated);

        assertEquals("Rutina avanzada", result.getName());
        assertFalse(result.getCertified());
        assertEquals(Timestamp.valueOf("2025-03-03 00:00:00"), result.getCreationDate());
        verify(routineRepository).save(routine);
    }

    @Test
    void updateRoutine_notFound_throwsException() {
        when(routineRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                routineService.updateRoutine(99L, routine));

        assertEquals("Routine not found with id: 99", ex.getMessage());
        verify(routineRepository, never()).save(any());
    }


    @Test
    void deleteRoutine_existing_deletesSuccessfully() {
        when(routineRepository.existsById(1L)).thenReturn(true);

        routineService.deleteRoutine(1L);

        verify(routineRepository).deleteById(1L);
    }

    @Test
    void deleteRoutine_notFound_throwsException() {
        when(routineRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                routineService.deleteRoutine(99L));

        assertEquals("Cannot delete. Routine not found with id: 99", ex.getMessage());
        verify(routineRepository, never()).deleteById(any());
    }


    @Test
    void getRoutinesByUser_returnsList() {
        List<Routine> routines = Arrays.asList(routine, new Routine());
        when(routineRepository.findByUserRoutines_User_Id(1L)).thenReturn(routines);

        List<Routine> result = routineService.getRoutinesByUser(1L);

        assertEquals(2, result.size());
        verify(routineRepository).findByUserRoutines_User_Id(1L);
    }


    @Test
    void getAllRoutines_returnsAll() {
        when(routineRepository.findAll()).thenReturn(List.of(routine));

        List<Routine> result = routineService.getAllRoutines();

        assertEquals(1, result.size());
        assertEquals("Rutina de fuerza", result.get(0).getName());
        verify(routineRepository).findAll();
    }

        @Test
    void getRoutineById_existing_returnsRoutine() {
        when(routineRepository.findById(1L)).thenReturn(Optional.of(routine));

        Routine result = routineService.getRoutineById(1L);

        assertNotNull(result);
        assertEquals("Rutina de fuerza", result.getName());
        verify(routineRepository).findById(1L);
    }

    @Test
    void getRoutineById_notFound_throwsException() {
        when(routineRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                routineService.getRoutineById(99L));

        assertEquals("Routine not found with id: 99", ex.getMessage());
        verify(routineRepository).findById(99L);
    }

}
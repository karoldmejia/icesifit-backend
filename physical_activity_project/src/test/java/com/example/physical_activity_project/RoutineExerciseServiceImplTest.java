package com.example.physical_activity_project;

import com.example.physical_activity_project.dto.RoutineExerciseDTO;
import com.example.physical_activity_project.model.Exercise;
import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.model.RoutineExercise;
import com.example.physical_activity_project.repository.IExerciseRepository;
import com.example.physical_activity_project.repository.IRoutineExerciseRepository;
import com.example.physical_activity_project.repository.IRoutineRepository;
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
    @Mock
    private IExerciseRepository exerciseRepository;
    @Mock
    private IRoutineRepository routineRepository;
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
        // DTO de entrada
        RoutineExerciseDTO dto = new RoutineExerciseDTO();
        dto.setSets(4);
        dto.setReps(10);
        dto.setTime(30);
        dto.setExerciseId(3L);
        dto.setRoutineId(2L);

        // Mockear Exercise y Routine existentes
        Exercise mockExercise = new Exercise();
        mockExercise.setId(3L);

        Routine mockRoutine = new Routine();
        mockRoutine.setId(2L);

        when(exerciseRepository.findById(3L)).thenReturn(Optional.of(mockExercise));
        when(routineRepository.findById(2L)).thenReturn(Optional.of(mockRoutine));

        // Mockear save
        when(routineExerciseRepository.save(any(RoutineExercise.class)))
                .thenAnswer(i -> i.getArgument(0));

        // Ejecutar
        RoutineExercise result = routineExerciseService.createRoutineExercise(dto);

        // Verificaciones
        assertNotNull(result);
        assertEquals(4, result.getSets());
        assertEquals(10, result.getReps());
        assertEquals(3L, result.getExercise().getId());
        assertEquals(2L, result.getRoutine().getId());

        verify(routineExerciseRepository).save(any(RoutineExercise.class));
        verify(exerciseRepository).findById(3L);
        verify(routineRepository).findById(2L);
    }

    @Test
    void updateRoutineExercise_existing_updatesAndReturns() {
        // DTO con los datos a actualizar
        RoutineExerciseDTO dto = new RoutineExerciseDTO();
        dto.setSets(5);
        dto.setReps(12);
        dto.setTime(60);
        dto.setRoutineId(2L);
        dto.setExerciseId(3L);

        // Entidades que mockean lo que se devuelve al buscar Exercise y Routine
        Routine mockRoutine = new Routine();
        mockRoutine.setId(2L);

        Exercise mockExercise = new Exercise();
        mockExercise.setId(3L);

        when(routineExerciseRepository.findById(1L)).thenReturn(Optional.of(routineExercise));
        when(exerciseRepository.findById(3L)).thenReturn(Optional.of(mockExercise));
        when(routineRepository.findById(2L)).thenReturn(Optional.of(mockRoutine));
        when(routineExerciseRepository.save(any(RoutineExercise.class))).thenAnswer(i -> i.getArgument(0));

        // Ejecutar
        RoutineExercise result = routineExerciseService.updateRoutineExercise(1L, dto);

        // Verificaciones
        assertEquals(5, result.getSets());
        assertEquals(12, result.getReps());
        assertEquals(60, result.getTime());
        assertEquals(2L, result.getRoutine().getId());
        assertEquals(3L, result.getExercise().getId());

        verify(routineExerciseRepository).save(routineExercise);
        verify(exerciseRepository).findById(3L);
        verify(routineRepository).findById(2L);
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
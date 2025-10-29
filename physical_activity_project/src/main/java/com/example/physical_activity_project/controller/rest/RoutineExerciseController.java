package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.RoutineExerciseDTO;
import com.example.physical_activity_project.mappers.RoutineExerciseMapper;
import com.example.physical_activity_project.model.RoutineExercise;
import com.example.physical_activity_project.services.IRoutineExerciseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/routine-exercises")
public class RoutineExerciseController {

    @Autowired
    private IRoutineExerciseService routineExerciseService;

    @Autowired
    private RoutineExerciseMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ASIGNAR_EJERCICIO_RUTINA')")
    public ResponseEntity<RoutineExerciseDTO> create(@RequestBody RoutineExerciseDTO dto) {
        RoutineExercise entity = mapper.dtoToEntity(dto);
        RoutineExercise saved = routineExerciseService.createRoutineExercise(entity);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VER_RUTINAS_PROPIAS') or hasAuthority('VER_TODAS_RUTINAS')")
    public ResponseEntity<List<RoutineExerciseDTO>> getAll() {
        List<RoutineExerciseDTO> list = routineExerciseService.getAllRoutineExercises()
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ASIGNAR_EJERCICIO_RUTINA')")
    public ResponseEntity<RoutineExerciseDTO> update(@PathVariable Long id, @RequestBody RoutineExerciseDTO dto) {
        RoutineExercise entity = mapper.dtoToEntity(dto);
        RoutineExercise updated = routineExerciseService.updateRoutineExercise(id, entity);
        return ResponseEntity.ok(mapper.entityToDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ELIMINAR_EJERCICIO_RUTINA')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        routineExerciseService.deleteRoutineExercise(id);
        return ResponseEntity.ok("RoutineExercise deleted successfully.");
    }

    @GetMapping("/routine/{routineId}")
    @PreAuthorize("hasAuthority('VER_RUTINAS_PROPIAS') or hasAuthority('VER_TODAS_RUTINAS')")
    public ResponseEntity<List<RoutineExerciseDTO>> getByRoutine(@PathVariable Long routineId) {
        List<RoutineExerciseDTO> list = routineExerciseService.getRoutineExercisesByRoutine(routineId)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/exercise/{exerciseId}")
    @PreAuthorize("hasAuthority('VER_EJERCICIOS')")
    public ResponseEntity<List<RoutineExerciseDTO>> getByExercise(@PathVariable Long exerciseId) {
        List<RoutineExerciseDTO> list = routineExerciseService.getRoutineExercisesByExercise(exerciseId)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}

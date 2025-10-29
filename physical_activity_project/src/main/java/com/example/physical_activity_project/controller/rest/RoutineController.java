package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.RoutineDTO;
import com.example.physical_activity_project.mappers.RoutineMapper;
import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.services.IRoutineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/routines")
public class RoutineController {

    @Autowired
    private IRoutineService routineService;

    @Autowired
    private RoutineMapper routineMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREAR_RUTINA') or hasAuthority('CREAR_RUTINA_CERTIFICADA')")
    public ResponseEntity<RoutineDTO> createRoutine(@RequestBody RoutineDTO routineDTO) {
        Routine routine = routineMapper.dtoToEntity(routineDTO);
        Routine saved = routineService.createRoutine(routine);
        return ResponseEntity.ok(routineMapper.entityToDto(saved));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VER_RUTINAS_PROPIAS') or hasAuthority('VER_TODAS_RUTINAS')")
    public ResponseEntity<List<RoutineDTO>> getAllRoutines() {
        List<RoutineDTO> routines = routineService.getAllRoutines()
                .stream()
                .map(routineMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(routines);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VER_RUTINAS_PROPIAS') or hasAuthority('VER_TODAS_RUTINAS')")

    public ResponseEntity<RoutineDTO> getRoutineById(@PathVariable Long id) {
        Routine routine = routineService.getRoutineById(id);
        return ResponseEntity.ok(routineMapper.entityToDto(routine));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDITAR_RUTINA_PROPIA') or hasAuthority('EDITAR_CUALQUIER_RUTINA')")
    public ResponseEntity<RoutineDTO> updateRoutine(@PathVariable Long id, @RequestBody RoutineDTO dto) {
        Routine updated = routineMapper.dtoToEntity(dto);
        Routine saved = routineService.updateRoutine(id, updated);
        return ResponseEntity.ok(routineMapper.entityToDto(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ELIMINAR_RUTINA_PROPIA') or hasAuthority('ELIMINAR_CUALQUIER_RUTINA')")
    public ResponseEntity<String> deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.ok("Routine deleted successfully.");
    }
}

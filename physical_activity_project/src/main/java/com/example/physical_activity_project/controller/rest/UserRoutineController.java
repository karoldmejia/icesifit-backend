package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.UserRoutineDTO;
import com.example.physical_activity_project.mappers.UserRoutineMapper;
import com.example.physical_activity_project.model.UserRoutine;
import com.example.physical_activity_project.services.IUserRoutineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/user-routines")
public class UserRoutineController {

    @Autowired
    private IUserRoutineService userRoutineService;

    @Autowired
    private UserRoutineMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ASIGNAR_RUTINA_USUARIO') or hasAuthority('CREAR_RUTINA')")
    public ResponseEntity<UserRoutineDTO> createUserRoutine(@RequestBody UserRoutineDTO dto) {
        UserRoutine entity = mapper.dtoToEntity(dto);
        UserRoutine saved = userRoutineService.assignRoutineToUser(entity);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VER_RUTINAS_ASIGNADAS') or hasAuthority('VER_TODAS_RUTINAS')")
    public ResponseEntity<List<UserRoutineDTO>> getAll() {
        List<UserRoutineDTO> list = userRoutineService.getAllUserRoutines()
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VER_RUTINAS_ASIGNADAS') or hasAuthority('VER_TODAS_RUTINAS')")
    public ResponseEntity<UserRoutineDTO> getById(@PathVariable Long id) {
        UserRoutine userRoutine = userRoutineService.getUserRoutineById(id);
        return ResponseEntity.ok(mapper.entityToDto(userRoutine));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ASIGNAR_RUTINA_USUARIO')")
    public ResponseEntity<UserRoutineDTO> update(@PathVariable Long id, @RequestBody UserRoutineDTO dto) {
        UserRoutine entity = mapper.dtoToEntity(dto);
        UserRoutine updated = userRoutineService.updateUserRoutine(id, entity);
        return ResponseEntity.ok(mapper.entityToDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ASIGNAR_RUTINA_USUARIO')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userRoutineService.deleteUserRoutine(id);
        return ResponseEntity.ok("UserRoutine deleted successfully.");
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('VER_RUTINAS_ASIGNADAS') or hasAuthority('VER_TODAS_RUTINAS')")
    public ResponseEntity<List<UserRoutineDTO>> getByUser(@PathVariable Long userId) {
        List<UserRoutineDTO> list = userRoutineService.getUserRoutinesByUser(userId)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/routine/{routineId}")
    @PreAuthorize("hasAuthority('VER_TODAS_RUTINAS')")
    public ResponseEntity<List<UserRoutineDTO>> getByRoutine(@PathVariable Long routineId) {
        List<UserRoutineDTO> list = userRoutineService.getUserRoutinesByRoutine(routineId)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}

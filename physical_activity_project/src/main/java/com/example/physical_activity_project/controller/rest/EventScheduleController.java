package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.EventScheduleDTO;
import com.example.physical_activity_project.mappers.EventScheduleMapper;
import com.example.physical_activity_project.model.EventSchedule;
import com.example.physical_activity_project.services.IEventScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-schedules")
@RequiredArgsConstructor
public class EventScheduleController {

    private final IEventScheduleService service;
    private final EventScheduleMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREAR_HORARIO')")
    public ResponseEntity<EventScheduleDTO> create(@RequestParam Long eventId, @RequestParam Long scheduleId) {
        EventSchedule saved = service.createEventSchedule(eventId, scheduleId);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDITAR_HORARIO')")
    public ResponseEntity<EventScheduleDTO> update(
            @PathVariable Long id,
            @RequestParam(required = false) Long newEventId,
            @RequestParam(required = false) Long newScheduleId
    ) {
        EventSchedule updated = service.updateEventSchedule(id, newEventId, newScheduleId);
        return ResponseEntity.ok(mapper.entityToDto(updated));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VER_HORARIOS')")
    public ResponseEntity<List<EventScheduleDTO>> getAll() {
        List<EventScheduleDTO> list = service.getAllEventSchedules().stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAuthority('VER_HORARIOS')")
    public ResponseEntity<List<EventScheduleDTO>> getByEvent(@PathVariable Long eventId) {
        List<EventScheduleDTO> list = service.getByEventId(eventId).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/schedule/{scheduleId}")
    @PreAuthorize("hasAuthority('VER_HORARIOS')")
    public ResponseEntity<List<EventScheduleDTO>> getBySchedule(@PathVariable Long scheduleId) {
        List<EventScheduleDTO> list = service.getByScheduleId(scheduleId).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ELIMINAR_HORARIO')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteEventSchedule(id);
        return ResponseEntity.ok().build();
    }
}

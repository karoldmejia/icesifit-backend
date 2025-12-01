package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.EventDTO;
import com.example.physical_activity_project.mappers.EventMapper;
import com.example.physical_activity_project.model.Event;
import com.example.physical_activity_project.services.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class EventController {

    private final IEventService eventService;
    private final EventMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREAR_EVENTO')")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO dto) {
        Event event = mapper.dtoToEntity(dto);
        Event saved = eventService.createEvent(event);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VER_EVENTOS')")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> list = eventService.getAllEvents().stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VER_EVENTOS')")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(mapper.entityToDto(event));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDITAR_EVENTO')")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO dto) {
        Event updated = mapper.dtoToEntity(dto);
        Event saved = eventService.updateEvent(id, updated);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ELIMINAR_EVENTO')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}

package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.UserEventDTO;
import com.example.physical_activity_project.mappers.UserEventMapper;
import com.example.physical_activity_project.model.UserEvent;
import com.example.physical_activity_project.services.IUserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-events")
@RequiredArgsConstructor
public class UserEventController {

    private final IUserEventService service;
    private final UserEventMapper mapper;

    @PostMapping
    public ResponseEntity<UserEventDTO> register(
            @RequestParam Long userId,
            @RequestParam Long eventId
    ) {
        UserEvent saved = service.registerUserToEvent(userId, eventId);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserEventDTO>> getUserEvents(@PathVariable Long userId) {
        List<UserEventDTO> list = service.getUserEvents(userId).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{userEventId}/attendance")
    public ResponseEntity<UserEventDTO> markAttendance(
            @PathVariable Long userEventId,
            @RequestParam Boolean attended
    ) {
        UserEvent updated = service.markAttendance(userEventId, attended);
        return ResponseEntity.ok(mapper.entityToDto(updated));
    }

    @DeleteMapping("/{userEventId}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long userEventId) {
        service.cancelRegistration(userEventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserEventDTO>> getAllUserEvents() {
        List<UserEventDTO> list = service.getAllUserEvents().stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<UserEventDTO>> getUserEventsByEvent(@PathVariable Long eventId) {
        List<UserEventDTO> list = service.getUserEventsByEvent(eventId).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

}

package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.ScheduleDTO;
import com.example.physical_activity_project.mappers.ScheduleMapper;
import com.example.physical_activity_project.model.Schedule;
import com.example.physical_activity_project.model.Space;
import com.example.physical_activity_project.services.IScheduleService;
import com.example.physical_activity_project.services.ISpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final IScheduleService scheduleService;
    private final ISpaceService spaceService;
    private final ScheduleMapper mapper;

    @PostMapping("/space/{spaceId}")
    public ResponseEntity<ScheduleDTO> createSchedule(@PathVariable Long spaceId, @RequestBody ScheduleDTO dto) {
        Schedule schedule = mapper.dtoToEntity(dto);
        // Asociar la space completa antes de guardar
        Space space = spaceService.getSpaceById(spaceId);
        schedule.setSpace(space);
        Schedule saved = scheduleService.createSchedule(spaceId, schedule);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        List<ScheduleDTO> list = scheduleService.getAllSchedules().stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/space/{spaceId}")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesBySpace(@PathVariable Long spaceId) {
        List<ScheduleDTO> list = scheduleService.getSchedulesBySpace(spaceId).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable Long id, @RequestBody ScheduleDTO dto) {
        Schedule updated = mapper.dtoToEntity(dto);
        Schedule saved = scheduleService.updateSchedule(id, updated);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }
}

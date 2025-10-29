package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.dto.ProgressDTO;
import com.example.physical_activity_project.mappers.ExerciseProgressMapper;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.services.impl.ExerciseProgressServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ExerciseProgressController {

    private final ExerciseProgressServiceImpl progressService;
    private final ExerciseProgressMapper mapper;

    @GetMapping
    public ResponseEntity<List<ExerciseProgressDTO>> getAll() {
        List<ExerciseProgressDTO> list = progressService.getAllProgress()
                .stream()
                .map(mapper::entityToDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExerciseProgressDTO>> getByUser(@PathVariable Long userId) {
        List<ExerciseProgressDTO> list = progressService.getProgressByUser(userId)
                .stream()
                .map(mapper::entityToDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<ExerciseProgressDTO> registerProgress(
            @PathVariable Long userId,
            @RequestBody ExerciseProgressDTO dto) {
        ExerciseProgress entity = mapper.dtoToEntity(dto);
        ExerciseProgress saved = progressService.registerProgress(userId, entity);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @PutMapping("/{progressId}")
    public ResponseEntity<ExerciseProgressDTO> updateProgress(
            @PathVariable Long progressId,
            @RequestBody ExerciseProgressDTO dto) {
        ExerciseProgress entity = mapper.dtoToEntity(dto);
        ExerciseProgress updated = progressService.updateProgress(progressId, entity);
        return ResponseEntity.ok(mapper.entityToDto(updated));
    }

    @DeleteMapping("/{progressId}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long progressId) {
        progressService.deleteProgress(progressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/week")
    public ResponseEntity<List<ExerciseProgressDTO>> getWeeklyProgress(
            @PathVariable Long userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        List<ExerciseProgressDTO> list = progressService.getProgressByWeek(userId, startDate)
                .stream()
                .map(mapper::entityToDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<ProgressDTO> getSummary(
            @PathVariable Long userId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        ProgressDTO summary = progressService.getProgressSummary(userId, start, end);
        return ResponseEntity.ok(summary);
    }
}

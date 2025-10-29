package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.RecommendationDTO;
import com.example.physical_activity_project.mappers.RecommendationMapper;
import com.example.physical_activity_project.model.Recommendation;
import com.example.physical_activity_project.services.IRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final IRecommendationService recommendationService;
    private final RecommendationMapper mapper;

    @PostMapping
    public ResponseEntity<RecommendationDTO> createRecommendation(
            @RequestParam Long trainerId,
            @RequestParam Long progressId,
            @RequestParam String content
    ) {
        Recommendation recommendation = recommendationService.createRecommendation(trainerId, progressId, content);
        return ResponseEntity.ok(mapper.entityToDto(recommendation));
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<RecommendationDTO>> getByTrainer(@PathVariable Long trainerId) {
        List<RecommendationDTO> recommendations = recommendationService.getRecommendationsByTrainer(trainerId)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationDTO>> getByUser(@PathVariable Long userId) {
        List<RecommendationDTO> recommendations = recommendationService.getRecommendationsByUser(userId)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recommendations);
    }

    @PutMapping("/{recommendationId}/status")
    public ResponseEntity<RecommendationDTO> updateStatus(
            @PathVariable Long recommendationId,
            @RequestParam String newStatus
    ) {
        Recommendation updated = recommendationService.updateRecommendationStatus(recommendationId, newStatus);
        return ResponseEntity.ok(mapper.entityToDto(updated));
    }

    @DeleteMapping("/{recommendationId}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long recommendationId) {
        recommendationService.deleteRecommendation(recommendationId);
        return ResponseEntity.noContent().build();
    }

}

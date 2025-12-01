package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.CreateRecommendationRequest;
import com.example.physical_activity_project.dto.RecommendationDTO;
import com.example.physical_activity_project.mappers.RecommendationMapper;
import com.example.physical_activity_project.model.Recommendation;
import com.example.physical_activity_project.services.IRecommendationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final IRecommendationService recommendationService;
    private final RecommendationMapper mapper;

    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasAuthority('VER_RECOMENDACIONES_CREADAS')")
    public ResponseEntity<List<RecommendationDTO>> getByTrainer(@PathVariable Long trainerId) {
        List<RecommendationDTO> recommendations = recommendationService.getRecommendationsByTrainer(trainerId)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('VER_RECOMENDACIONES_PROPIAS') or hasAuthority('VER_RECOMENDACIONES_CREADAS')")
    public ResponseEntity<List<RecommendationDTO>> getByUser(@PathVariable Long userId) {
        List<RecommendationDTO> recommendations = recommendationService.getRecommendationsByUser(userId)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recommendations);
    }

    @PutMapping("/{recommendationId}/status")
    @PreAuthorize("hasAuthority('ACTUALIZAR_ESTADO_RECOMENDACION')")
    public ResponseEntity<RecommendationDTO> updateStatus(
            @PathVariable Long recommendationId,
            @RequestParam String newStatus
    ) {
        Recommendation updated = recommendationService.updateRecommendationStatus(recommendationId, newStatus);
        return ResponseEntity.ok(mapper.entityToDto(updated));
    }

    @DeleteMapping("/{recommendationId}")
    @PreAuthorize("hasAuthority('ELIMINAR_RECOMENDACION')")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long recommendationId) {
        recommendationService.deleteRecommendation(recommendationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREAR_RECOMENDACION')")
    public ResponseEntity<RecommendationDTO> createRecommendation(
            @RequestBody CreateRecommendationRequest request
    ) {
        Recommendation recommendation = recommendationService.createRecommendation(
                request.getTrainerId(),
                request.getProgressId(),
                request.getContent()
        );
        return ResponseEntity.ok(mapper.entityToDto(recommendation));
    }
}

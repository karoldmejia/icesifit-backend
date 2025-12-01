package com.example.physical_activity_project.dto;

import lombok.Data;

@Data
public class CreateRecommendationRequest {
    private Long trainerId;
    private Long progressId;
    private String content;
}
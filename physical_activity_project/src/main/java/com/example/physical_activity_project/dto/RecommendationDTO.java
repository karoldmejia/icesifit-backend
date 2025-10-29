package com.example.physical_activity_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {
    private Long id;
    private Timestamp recommendationDate;
    private String content;
    private String status;

    private Long trainerId;
    private String trainerName;
    private Long progressId;
    private Long userId;
}

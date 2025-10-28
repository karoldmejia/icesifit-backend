package com.example.physical_activity_project.dto;

import lombok.Data;

@Data
public class ExerciseDTO {
    private Long id;
    private String name;
    private String type;
    private String description;
    private Double duration;
    private String difficulty;
    private String videoUrl;
}

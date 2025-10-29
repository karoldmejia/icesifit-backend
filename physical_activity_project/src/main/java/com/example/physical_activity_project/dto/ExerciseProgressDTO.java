package com.example.physical_activity_project.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class ExerciseProgressDTO {

    private Long id;
    private Timestamp progressDate;
    private Integer setsCompleted;
    private Integer repsCompleted;
    private Integer timeCompleted;
    private Integer effortLevel;

    private Long userId;
    private Long routineExerciseId;
}

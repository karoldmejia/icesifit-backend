package com.example.physical_activity_project.dto;

import lombok.Data;

@Data
public class RoutineExerciseDTO {
    private Long id;
    private Integer sets;
    private Integer reps;
    private Integer time;
    private Long exerciseId;
    private Long routineId;
    private Long userRoutineId;
}


package com.example.physical_activity_project.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class RoutineDTO {
    private Long id;
    private String name;
    private Timestamp creationDate;
    private Boolean certified;
}

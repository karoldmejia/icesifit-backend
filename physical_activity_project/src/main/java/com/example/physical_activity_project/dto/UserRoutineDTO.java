package com.example.physical_activity_project.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class UserRoutineDTO {
    private Long id;
    private Timestamp assignmentDate;
    private Boolean status;
    private Long routineId;
    private Long userId;
}

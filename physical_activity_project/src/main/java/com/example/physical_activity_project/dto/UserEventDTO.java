package com.example.physical_activity_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDTO {
    private Long id;
    private Timestamp registrationDate;
    private Boolean attended;

    // Información básica del user
    private Long userId;
    private String userName;

    // Información básica del event
    private Long eventId;
    private String eventName;
}

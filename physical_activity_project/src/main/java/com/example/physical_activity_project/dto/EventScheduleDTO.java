package com.example.physical_activity_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventScheduleDTO {
    private Long id;

    // Información básica del schedule
    private Long scheduleId;
    private String scheduleStartTime;
    private String scheduleEndTime;

    // Información básica del event
    private Long eventId;
    private String eventName;
}

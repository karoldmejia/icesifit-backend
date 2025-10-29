package com.example.physical_activity_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String originType;
    private Integer originId;
    private String text;
    private Timestamp sentDate;
    private Boolean readFlag;

    private Long userId;
    private String userName;
}

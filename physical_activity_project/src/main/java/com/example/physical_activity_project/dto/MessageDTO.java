package com.example.physical_activity_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String content;
    private Timestamp sendDate;

    private Long userId;
    private String userName;

    private Long trainerId;
    private String trainerName;
}

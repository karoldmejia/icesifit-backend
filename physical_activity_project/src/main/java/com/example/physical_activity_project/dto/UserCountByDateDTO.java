package com.example.physical_activity_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class UserCountByDateDTO {
    private Date date;
    private Long userCount;
}

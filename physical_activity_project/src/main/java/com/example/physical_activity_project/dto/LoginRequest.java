package com.example.physical_activity_project.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String institutionalEmail;
    private String password;
}
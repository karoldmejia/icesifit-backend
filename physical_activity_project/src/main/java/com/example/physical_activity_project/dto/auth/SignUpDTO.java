package com.example.physical_activity_project.dto.auth;

import lombok.Data;

@Data
public class SignUpDTO {
    private String name;
    private String institutionalEmail;
    private String password;
}

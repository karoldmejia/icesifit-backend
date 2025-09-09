package com.example.physical_activity_project.model;

import jakarta.persistence.Column;

import java.sql.Timestamp;

public class UserTrainerAssignment {
    private Long user_id;
    private Long trainer_id;

    @Column(nullable = false)
    // Cambiar en el MR a Timestamp
    private Timestamp assignment_date;
    @Column(nullable = false, length = 20)
    private String status;
}

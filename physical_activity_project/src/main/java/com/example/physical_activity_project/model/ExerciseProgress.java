package com.example.physical_activity_project.model;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.sql.Timestamp;

public class ExerciseProgress {
    private Long id;
    @Column(nullable = false)
    private Timestamp progress_date;
    @Column(nullable = false)
    private Integer sets_completed;
    private Integer reps_completed;
    private Integer time_completed;
    private Integer effort_level;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

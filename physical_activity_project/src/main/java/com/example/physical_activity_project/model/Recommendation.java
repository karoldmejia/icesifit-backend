package com.example.physical_activity_project.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

public class Recommendation {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Timestamp recommendation_date;
    @Column(nullable = false, length = 500)
    private String content;
    @Column(nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "progress_id", nullable = false)
    private ExerciseProgress exerciseProgress;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;
}

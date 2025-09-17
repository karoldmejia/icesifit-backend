package com.example.physical_activity_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recommendation {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "recommendation_date", nullable = false)
    private Timestamp recommendationDate;
    @Column(name = "content",nullable = false, length = 500)
    private String content;
    @Column(name = "status",nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "progress_id", nullable = false)
    private ExerciseProgress exerciseProgress;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;
}

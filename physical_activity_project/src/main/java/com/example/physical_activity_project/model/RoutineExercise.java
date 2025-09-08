package com.example.physical_activity_project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RoutineExercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sets", nullable = false)
    private Integer sets;
    @Column(name = "reps", nullable = true)
    private Integer reps;
    @Column(name = "time", nullable = true)
    private Integer time;

    @ManyToOne
    @JoinColumn(name = "exercise", nullable = false)
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "routine", nullable = false)
    private Routine routine;

}

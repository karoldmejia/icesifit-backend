package com.example.physical_activity_project.model;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="routine")
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="name" , nullable = false, length = 100)
    private String name;
    @Column(name ="creation_date" , nullable = false)
    private Timestamp creationDate;
    @Column(name = "certified", nullable = false)
    private Boolean certified;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RoutineExercise> routineExercises; // solo ejercicios base

    @OneToMany(mappedBy = "routine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserRoutine> userRoutines;

}

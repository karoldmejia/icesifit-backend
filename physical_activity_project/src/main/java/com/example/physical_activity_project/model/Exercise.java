package com.example.physical_activity_project.model;

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
@Table(name="exercise")
public class Exercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "type", nullable = false, length = 100)
    private String type;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "duration", nullable = true)
    private Double duration;
    @Column(name = "difficulty", nullable = false, length = 50)
    private String difficulty;
    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @OneToMany(mappedBy = "exercise", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RoutineExercise> routineExercises;
}

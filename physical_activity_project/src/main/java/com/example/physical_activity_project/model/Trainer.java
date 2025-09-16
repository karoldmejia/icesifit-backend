package com.example.physical_activity_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Trainer extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "certification", nullable = false)
    private String certification;
    @Column(name = "specialty", nullable = false)
    private String specialty;

    @OneToMany(mappedBy = "trainer", fetch = jakarta.persistence.FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messages;

    @OneToMany(mappedBy = "trainer", fetch = jakarta.persistence.FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Recommendation> recommendations;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserTrainerAssignment> userTrainerAssignments;
}

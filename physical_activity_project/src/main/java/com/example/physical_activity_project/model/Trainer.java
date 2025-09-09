package com.example.physical_activity_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
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
    private List<Message> messages;

    @OneToMany(mappedBy = "trainer", fetch = jakarta.persistence.FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Recommendation> recommendations;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserTrainerAssignment> userTrainerAssignments;
}

package com.example.physical_activity_project.model;

import jakarta.persistence.*;

import java.util.List;

public class Trainer {
    // REVISAR EL ID
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String certification;
    @Column(nullable = false)
    private String specialty;

    @OneToMany(mappedBy = "trainer", fetch = jakarta.persistence.FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "trainer", fetch = jakarta.persistence.FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Recommendation> recommendations;
}

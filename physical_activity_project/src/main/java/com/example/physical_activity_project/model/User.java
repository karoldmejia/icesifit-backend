package com.example.physical_activity_project.model;

import jakarta.persistence.*;

import java.util.List;

@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false)
    private String institutional_email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 50)
    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ExerciseProgress> exerciseProgresses;
}

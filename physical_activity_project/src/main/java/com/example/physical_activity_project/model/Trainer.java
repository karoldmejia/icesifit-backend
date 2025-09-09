package com.example.physical_activity_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Trainer extends User {
    // REVISAR EL ID
    @Column(nullable = false)
    private String certification;
    @Column(nullable = false)
    private String specialty;

    @OneToMany(mappedBy = "trainer", fetch = jakarta.persistence.FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "trainer", fetch = jakarta.persistence.FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Recommendation> recommendations;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserTrainerAssignment> userTrainerAssignments;
}

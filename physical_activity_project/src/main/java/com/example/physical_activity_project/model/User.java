package com.example.physical_activity_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "institutional_email", nullable = false, unique = true)
    private String institutionalEmail;

    private String password;

    // Importante colocarlo en el MER
    private Timestamp createdAt;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}

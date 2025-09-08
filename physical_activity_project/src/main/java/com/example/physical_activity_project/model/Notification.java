package com.example.physical_activity_project.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "origin_type", nullable = true)
    private String originType;
    //id -> origin id
    @Column(name = "text", nullable = false)
    private String text;
    @Column(name = "sent_date", nullable = false)
    private Timestamp sentDate;
    @Column(name = "read_flag", nullable = false)
    private Boolean readFlag;
}

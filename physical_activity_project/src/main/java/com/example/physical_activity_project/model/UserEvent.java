package com.example.physical_activity_project.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEvent {
    @Id
    
    private Long id;

    private Timestamp registrationDate;

    private Boolean attended;
}

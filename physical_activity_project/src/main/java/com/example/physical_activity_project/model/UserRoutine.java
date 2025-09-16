package com.example.physical_activity_project.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_routine")
public class UserRoutine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="assignment_date" , nullable = false)
    private Timestamp assignmentDate;
    @Column(name ="status" , nullable = false, length = 20)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name="routine_id", nullable = false)
    private Routine routine;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

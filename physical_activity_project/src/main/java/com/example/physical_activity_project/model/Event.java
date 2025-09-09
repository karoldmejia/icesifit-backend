package com.example.physical_activity_project.model;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "type", nullable = true)
    private String type;
    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;
    @Column(name = "end_date", nullable = false)
    private Timestamp endDate;
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    @Column(name = "location", nullable = true)
    private String location;
    @Column(name = "description", nullable = true)
    private String description;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EventSchedule> eventSchedules;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserEvent> UserEvents;

    
}

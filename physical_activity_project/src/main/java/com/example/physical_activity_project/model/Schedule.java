package com.example.physical_activity_project.model;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "day_of_week", nullable = true , length = 20)
    private String dayOfWeek;
    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;
    @Column(name = "end_time", nullable = false)
    private Timestamp endTime;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<EventSchedule> eventSchedules;
}

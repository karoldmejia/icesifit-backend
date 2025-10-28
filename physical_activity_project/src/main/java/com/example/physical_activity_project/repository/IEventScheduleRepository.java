package com.example.physical_activity_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.physical_activity_project.model.EventSchedule;

import java.util.List;

public interface IEventScheduleRepository extends JpaRepository<EventSchedule, Long>{
    List<EventSchedule> findByEventId(Long eventId);
    List<EventSchedule> findByScheduleId(Long scheduleId);
}

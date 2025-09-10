package com.example.physical_activity_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.physical_activity_project.model.Schedule;

public interface IScheduleRepository extends JpaRepository<Schedule, Long>{
    
}

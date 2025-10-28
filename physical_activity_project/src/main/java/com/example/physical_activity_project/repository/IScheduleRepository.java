package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.physical_activity_project.model.Schedule;

import java.util.List;

public interface IScheduleRepository extends JpaRepository<Schedule, Long>{
    List<Schedule> findBySpaceId(Long spaceId);

}

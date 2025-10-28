package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.Schedule;

import java.util.List;

public interface IScheduleService {
    Schedule createSchedule(Long spaceId, Schedule schedule);
    List<Schedule> getAllSchedules();
    List<Schedule> getSchedulesBySpace(Long spaceId);
    Schedule updateSchedule(Long id, Schedule updatedSchedule);
    void deleteSchedule(Long id);
}

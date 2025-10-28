package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.EventSchedule;

import java.util.List;

public interface IEventScheduleService {
    EventSchedule createEventSchedule(Long eventId, Long scheduleId);
    EventSchedule updateEventSchedule(Long id, Long newEventId, Long newScheduleId);
    List<EventSchedule> getAllEventSchedules();
    List<EventSchedule> getByEventId(Long eventId);
    List<EventSchedule> getByScheduleId(Long scheduleId);
    void deleteEventSchedule(Long id);
}

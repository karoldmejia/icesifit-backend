package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.UserEvent;

import java.util.List;

public interface IUserEventService {
    UserEvent registerUserToEvent(Long userId, Long eventId);
    List<UserEvent> getUserEvents(Long userId);
    UserEvent markAttendance(Long userEventId, Boolean attended);
    void cancelRegistration(Long userEventId);
    List<UserEvent> getAllUserEvents();
    List<UserEvent> getUserEventsByEvent(Long eventId);
}

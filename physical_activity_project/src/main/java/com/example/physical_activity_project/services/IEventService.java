package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.Event;

import java.util.List;

public interface IEventService {
    Event createEvent(Event event);
    List<Event> getAllEvents();
    Event getEventById(Long id);
    Event updateEvent(Long id, Event updated);
    void deleteEvent(Long id);
}

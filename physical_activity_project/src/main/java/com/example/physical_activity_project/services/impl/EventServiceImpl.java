package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.Event;
import com.example.physical_activity_project.repository.IEventRepository;
import com.example.physical_activity_project.services.IEventService;
import com.example.physical_activity_project.services.INotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements IEventService {

    private final IEventRepository eventRepository;
    private final INotificationService notificationService;

    public EventServiceImpl(IEventRepository eventRepository,
                            INotificationService notificationService) {
        this.eventRepository = eventRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Event createEvent(Event event) {
        Event saved = eventRepository.save(event);

        notificationService.sendNotificationToAll(
                "¡Nuevo evento disponible: " + saved.getName() + "!",
                "EVENT_CREATE",
                Math.toIntExact(saved.getId())
        );

        return saved;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Override
    public Event updateEvent(Long id, Event updated) {
        Event existing = getEventById(id);

        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setCapacity(updated.getCapacity());
        existing.setDescription(updated.getDescription());

        Event saved = eventRepository.save(existing);

        notificationService.sendNotificationToAll(
                "El evento '" + saved.getName() + "' ha sido actualizado.",
                "EVENT_UPDATE",
                Math.toIntExact(saved.getId())
        );

        return saved;
    }

    @Override
    public void deleteEvent(Long id) {
        Event existing = getEventById(id);
        eventRepository.delete(existing);

        notificationService.sendNotificationToAll(
                "El evento '" + existing.getName() + "' ha sido cancelado.",
                "EVENT_DELETE",
                Math.toIntExact(id)
        );
    }
}

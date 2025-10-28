package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.Event;
import com.example.physical_activity_project.model.EventSchedule;
import com.example.physical_activity_project.model.Schedule;
import com.example.physical_activity_project.repository.IEventRepository;
import com.example.physical_activity_project.repository.IEventScheduleRepository;
import com.example.physical_activity_project.repository.IScheduleRepository;
import com.example.physical_activity_project.services.IEventScheduleService;
import com.example.physical_activity_project.services.INotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventScheduleServiceImpl implements IEventScheduleService {

    private final IEventScheduleRepository eventScheduleRepository;
    private final IEventRepository eventRepository;
    private final IScheduleRepository scheduleRepository;
    private final INotificationService notificationService;

    public EventScheduleServiceImpl(IEventScheduleRepository eventScheduleRepository,
                                    IEventRepository eventRepository,
                                    IScheduleRepository scheduleRepository,
                                    INotificationService notificationService) {
        this.eventScheduleRepository = eventScheduleRepository;
        this.eventRepository = eventRepository;
        this.scheduleRepository = scheduleRepository;
        this.notificationService = notificationService;
    }

    @Override
    public EventSchedule createEventSchedule(Long eventId, Long scheduleId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        EventSchedule eventSchedule = new EventSchedule();
        eventSchedule.setEvent(event);
        eventSchedule.setSchedule(schedule);

        EventSchedule saved = eventScheduleRepository.save(eventSchedule);

        notificationService.sendNotificationToAll(
                "El evento '" + event.getName() + "' ahora tiene un nuevo horario.",
                "EVENT_SCHEDULE_CREATED",
                Math.toIntExact(saved.getId())
        );

        return saved;
    }

    @Override
    public EventSchedule updateEventSchedule(Long id, Long newEventId, Long newScheduleId) {
        EventSchedule existing = eventScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EventSchedule not found"));

        boolean updated = false;
        String oldEventName = existing.getEvent().getName();

        if (newEventId != null && !newEventId.equals(existing.getEvent().getId())) {
            Event newEvent = eventRepository.findById(newEventId)
                    .orElseThrow(() -> new RuntimeException("New event not found"));
            existing.setEvent(newEvent);
            updated = true;
        }

        if (newScheduleId != null && !newScheduleId.equals(existing.getSchedule().getId())) {
            Schedule newSchedule = scheduleRepository.findById(newScheduleId)
                    .orElseThrow(() -> new RuntimeException("New schedule not found"));
            existing.setSchedule(newSchedule);
            updated = true;
        }

        if (updated) {
            EventSchedule saved = eventScheduleRepository.save(existing);

            notificationService.sendNotificationToAll(
                    "El horario del evento '" + oldEventName + "' ha sido actualizado.",
                    "EVENT_SCHEDULE_UPDATED",
                    Math.toIntExact(saved.getId())
            );

            return saved;
        } else {
            throw new RuntimeException("No changes detected for EventSchedule");
        }
    }

    @Override
    public List<EventSchedule> getAllEventSchedules() {
        return eventScheduleRepository.findAll();
    }

    @Override
    public List<EventSchedule> getByEventId(Long eventId) {
        return eventScheduleRepository.findByEventId(eventId);
    }

    @Override
    public List<EventSchedule> getByScheduleId(Long scheduleId) {
        return eventScheduleRepository.findByScheduleId(scheduleId);
    }

    @Override
    public void deleteEventSchedule(Long id) {
        EventSchedule existing = eventScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EventSchedule not found"));
        eventScheduleRepository.delete(existing);

        notificationService.sendNotificationToAll(
                "Se ha eliminado un horario del evento '" + existing.getEvent().getName() + "'.",
                "EVENT_SCHEDULE_DELETED",
                Math.toIntExact(existing.getId())
        );
    }
}

package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Event;
import com.example.physical_activity_project.repository.IEventRepository;
import com.example.physical_activity_project.services.INotificationService;
import com.example.physical_activity_project.services.impl.EventServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    private IEventRepository eventRepository;
    private INotificationService notificationService;
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        eventRepository = mock(IEventRepository.class);
        notificationService = mock(INotificationService.class);
        eventService = new EventServiceImpl(eventRepository, notificationService);
    }

    @Test
    void createEvent_ShouldSaveEventAndSendNotification() {
        Event event = new Event();
        event.setId(1L);
        event.setName("Yoga Session");

        when(eventRepository.save(event)).thenReturn(event);

        Event result = eventService.createEvent(event);

        assertEquals(event, result);
        verify(eventRepository).save(event);

        verify(notificationService).sendNotificationToAll(
            "¡Nuevo evento disponible: Yoga Session!",
            "EVENT_CREATE",
            1
        );
    }

    @Test
    void getAllEvents_ShouldReturnListOfEvents() {
        List<Event> events = List.of(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.getAllEvents();

        assertEquals(2, result.size());
        verify(eventRepository).findAll();
    }

    @Test
    void getEventById_ShouldReturnEvent_WhenExists() {
        Event event = new Event();
        event.setId(5L);
        when(eventRepository.findById(5L)).thenReturn(Optional.of(event));

        Event result = eventService.getEventById(5L);

        assertEquals(5L, result.getId());
    }

    @Test
    void getEventById_ShouldThrowException_WhenNotFound() {
        when(eventRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> eventService.getEventById(10L));

        assertEquals("Event not found", ex.getMessage());
    }

    @Test
    void updateEvent_ShouldModifyEventAndSendNotification() {
        Event existing = new Event();
        existing.setId(1L);
        existing.setName("Old Event");
        existing.setType("Workshop");

        Event updated = new Event();
        updated.setName("New Event");
        updated.setType("Conference");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArgument(0));

        Event result = eventService.updateEvent(1L, updated);

        assertEquals("New Event", result.getName());
        assertEquals("Conference", result.getType());
        verify(notificationService).sendNotificationToAll(
            "El evento 'New Event' ha sido actualizado.",
            "EVENT_UPDATE",
            1
        );

    }

    @Test
    void deleteEvent_ShouldRemoveEventAndSendNotification() {
        Event existing = new Event();
        existing.setId(2L);
        existing.setName("Maratón");

        when(eventRepository.findById(2L)).thenReturn(Optional.of(existing));

        eventService.deleteEvent(2L);

        verify(eventRepository).delete(existing);
        verify(notificationService).sendNotificationToAll(
            "El evento 'Maratón' ha sido cancelado.",
            "EVENT_DELETE",
            2
        );
    }
}

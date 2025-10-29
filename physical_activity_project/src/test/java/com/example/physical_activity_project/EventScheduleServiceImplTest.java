package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Event;
import com.example.physical_activity_project.model.EventSchedule;
import com.example.physical_activity_project.model.Schedule;
import com.example.physical_activity_project.repository.IEventRepository;
import com.example.physical_activity_project.repository.IEventScheduleRepository;
import com.example.physical_activity_project.repository.IScheduleRepository;
import com.example.physical_activity_project.services.INotificationService;
import com.example.physical_activity_project.services.impl.EventScheduleServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EventScheduleServiceImplTest {

    @Mock
    private IEventScheduleRepository eventScheduleRepository;
    @Mock
    private IEventRepository eventRepository;
    @Mock
    private IScheduleRepository scheduleRepository;
    @Mock
    private INotificationService notificationService;

    @InjectMocks
    private EventScheduleServiceImpl eventScheduleService;

    private Event event;
    private Schedule schedule;
    private EventSchedule eventSchedule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        event = new Event();
        event.setId(1L);
        event.setName("Yoga Day");

        schedule = new Schedule();
        schedule.setId(10L);

        eventSchedule = new EventSchedule();
        eventSchedule.setId(100L);
        eventSchedule.setEvent(event);
        eventSchedule.setSchedule(schedule);
    }
    @Test
    void createEventSchedule_success() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(scheduleRepository.findById(10L)).thenReturn(Optional.of(schedule));
        when(eventScheduleRepository.save(any(EventSchedule.class))).thenReturn(eventSchedule);

        EventSchedule result = eventScheduleService.createEventSchedule(1L, 10L);

        assertNotNull(result);
        assertEquals(event, result.getEvent());
        assertEquals(schedule, result.getSchedule());
        verify(notificationService).sendNotificationToAll(
                contains("Yoga Day"),
                eq("EVENT_SCHEDULE_CREATED"),
                eq(100)
        );
    }

    @Test
    void createEventSchedule_eventNotFound_throwsException() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> eventScheduleService.createEventSchedule(1L, 10L));
        assertEquals("Event not found", ex.getMessage());
    }

    @Test
    void createEventSchedule_scheduleNotFound_throwsException() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(scheduleRepository.findById(10L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> eventScheduleService.createEventSchedule(1L, 10L));
        assertEquals("Schedule not found", ex.getMessage());
    }

    @Test
    void updateEventSchedule_changeBothEventAndSchedule_success() {
        Event newEvent = new Event();
        newEvent.setId(2L);
        newEvent.setName("Crossfit");

        Schedule newSchedule = new Schedule();
        newSchedule.setId(20L);

        when(eventScheduleRepository.findById(100L)).thenReturn(Optional.of(eventSchedule));
        when(eventRepository.findById(2L)).thenReturn(Optional.of(newEvent));
        when(scheduleRepository.findById(20L)).thenReturn(Optional.of(newSchedule));
        when(eventScheduleRepository.save(any(EventSchedule.class))).thenReturn(eventSchedule);

        EventSchedule result = eventScheduleService.updateEventSchedule(100L, 2L, 20L);

        assertNotNull(result);
        verify(notificationService).sendNotificationToAll(
                contains("Yoga Day"),
                eq("EVENT_SCHEDULE_UPDATED"),
                eq(100)
        );
    }

    @Test
    void updateEventSchedule_noChangesDetected_throwsException() {
        when(eventScheduleRepository.findById(100L)).thenReturn(Optional.of(eventSchedule));
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> eventScheduleService.updateEventSchedule(100L, 1L, 10L));
        assertEquals("No changes detected for EventSchedule", ex.getMessage());
    }

    @Test
    void updateEventSchedule_eventNotFound_throwsException() {
        when(eventScheduleRepository.findById(100L)).thenReturn(Optional.of(eventSchedule));
        when(eventRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> eventScheduleService.updateEventSchedule(100L, 2L, null));
        assertEquals("New event not found", ex.getMessage());
    }

    @Test
    void updateEventSchedule_scheduleNotFound_throwsException() {
        when(eventScheduleRepository.findById(100L)).thenReturn(Optional.of(eventSchedule));
        when(scheduleRepository.findById(20L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> eventScheduleService.updateEventSchedule(100L, null, 20L));
        assertEquals("New schedule not found", ex.getMessage());
    }

    @Test
    void getAllEventSchedules_success() {
        when(eventScheduleRepository.findAll()).thenReturn(List.of(eventSchedule));
        List<EventSchedule> list = eventScheduleService.getAllEventSchedules();
        assertEquals(1, list.size());
    }

    @Test
    void getByEventId_success() {
        when(eventScheduleRepository.findByEventId(1L)).thenReturn(List.of(eventSchedule));
        List<EventSchedule> list = eventScheduleService.getByEventId(1L);
        assertEquals(1, list.size());
    }

    @Test
    void getByScheduleId_success() {
        when(eventScheduleRepository.findByScheduleId(10L)).thenReturn(List.of(eventSchedule));
        List<EventSchedule> list = eventScheduleService.getByScheduleId(10L);
        assertEquals(1, list.size());
    }

    @Test
    void deleteEventSchedule_success() {
        when(eventScheduleRepository.findById(100L)).thenReturn(Optional.of(eventSchedule));

        eventScheduleService.deleteEventSchedule(100L);

        verify(eventScheduleRepository).delete(eventSchedule);
        verify(notificationService).sendNotificationToAll(
                contains("Yoga Day"),
                eq("EVENT_SCHEDULE_DELETED"),
                eq(100)
        );
    }

    @Test
    void deleteEventSchedule_notFound_throwsException() {
        when(eventScheduleRepository.findById(100L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> eventScheduleService.deleteEventSchedule(100L));
        assertEquals("EventSchedule not found", ex.getMessage());
    }

    @Test
    void updateEventSchedule_notFound_throwsException() {
        when(eventScheduleRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> eventScheduleService.updateEventSchedule(999L, 1L, 10L));

        assertEquals("EventSchedule not found", ex.getMessage());
    }

    @Test
    void updateEventSchedule_onlyScheduleChanged_success() {
        Schedule newSchedule = new Schedule();
        newSchedule.setId(99L);

        when(eventScheduleRepository.findById(100L)).thenReturn(Optional.of(eventSchedule));
        when(scheduleRepository.findById(99L)).thenReturn(Optional.of(newSchedule));
        when(eventScheduleRepository.save(any(EventSchedule.class))).thenReturn(eventSchedule);

        EventSchedule result = eventScheduleService.updateEventSchedule(100L, null, 99L);

        assertNotNull(result);
        verify(notificationService).sendNotificationToAll(
                contains("Yoga Day"),
                eq("EVENT_SCHEDULE_UPDATED"),
                anyInt()
        );
    }

    @Test
    void updateEventSchedule_changesSchedule_success() {
        Event existingEvent = new Event();
        existingEvent.setId(10L);
        existingEvent.setName("Evento Original");

        Schedule oldSchedule = new Schedule();
        oldSchedule.setId(1L);

        Schedule newSchedule = new Schedule();
        newSchedule.setId(2L);

        EventSchedule existing = new EventSchedule();
        existing.setId(100L);
        existing.setEvent(existingEvent);
        existing.setSchedule(oldSchedule);

        when(eventScheduleRepository.findById(100L)).thenReturn(Optional.of(existing));
        when(scheduleRepository.findById(2L)).thenReturn(Optional.of(newSchedule));
        when(eventScheduleRepository.save(any(EventSchedule.class))).thenReturn(existing);

        EventSchedule result = eventScheduleService.updateEventSchedule(100L, null, 2L);

        assertNotNull(result);
        verify(eventScheduleRepository, times(1)).save(existing);
        verify(notificationService, times(1)).sendNotificationToAll(
                contains("Evento Original"),
                eq("EVENT_SCHEDULE_UPDATED"),
                anyInt()
        );
    }

    @Test
    void updateEventSchedule_changeOnlySchedule_triggersScheduleIfBlock() {
        Event event = new Event();
        event.setId(1L);
        event.setName("Yoga Original");

        Schedule oldSchedule = new Schedule();
        oldSchedule.setId(1L);

        Schedule newSchedule = new Schedule();
        newSchedule.setId(2L);

        EventSchedule existing = new EventSchedule();
        existing.setId(100L);
        existing.setEvent(event);
        existing.setSchedule(oldSchedule);

        when(eventScheduleRepository.findById(100L)).thenReturn(Optional.of(existing));
        when(scheduleRepository.findById(2L)).thenReturn(Optional.of(newSchedule));
        when(eventScheduleRepository.save(any(EventSchedule.class))).thenReturn(existing);

        EventSchedule result = eventScheduleService.updateEventSchedule(100L, null, 2L);

        assertNotNull(result);
        assertEquals(newSchedule, existing.getSchedule());
        verify(scheduleRepository, times(1)).findById(2L);
        verify(eventScheduleRepository, times(1)).save(existing);
        verify(notificationService).sendNotificationToAll(
                contains("Yoga Original"),
                eq("EVENT_SCHEDULE_UPDATED"),
                anyInt()
        );
    }
}

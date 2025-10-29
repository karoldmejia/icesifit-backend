package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Event;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserEvent;
import com.example.physical_activity_project.repository.IEventRepository;
import com.example.physical_activity_project.repository.IUserEventRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.INotificationService;
import com.example.physical_activity_project.services.impl.UserEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserEventServiceImplTest {

    @Mock
    private IUserEventRepository userEventRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IEventRepository eventRepository;

    @Mock
    private INotificationService notificationService;

    @InjectMocks
    private UserEventServiceImpl userEventService;

    private User user;
    private Event event;
    private UserEvent userEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Heiner");

        event = new Event();
        event.setId(100L);
        event.setName("Maratón 2025");

        userEvent = new UserEvent();
        userEvent.setId(10L);
        userEvent.setUser(user);
        userEvent.setEvent(event);
        userEvent.setAttended(false);
        userEvent.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void shouldRegisterUserToEventSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(100L)).thenReturn(Optional.of(event));
        when(userEventRepository.save(any(UserEvent.class))).thenReturn(userEvent);

        UserEvent result = userEventService.registerUserToEvent(1L, 100L);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(event, result.getEvent());
        assertFalse(result.getAttended());

        verify(userEventRepository, times(1)).save(any(UserEvent.class));
        verify(notificationService, times(1))
                .sendNotification(eq(1L), contains("Maratón 2025"), eq("USER_EVENT_CREATE"), eq(10));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userEventService.registerUserToEvent(99L, 100L)
        );
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userEventService.registerUserToEvent(1L, 999L)
        );
        assertEquals("Event not found", ex.getMessage());
    }

    @Test
    void shouldReturnUserEvents() {
        when(userEventRepository.findByUserId(1L)).thenReturn(List.of(userEvent));

        List<UserEvent> result = userEventService.getUserEvents(1L);

        assertEquals(1, result.size());
        assertEquals(userEvent, result.get(0));
        verify(userEventRepository, times(1)).findByUserId(1L);
    }

    @Test
    void shouldMarkAttendanceAsTrue() {
        when(userEventRepository.findById(10L)).thenReturn(Optional.of(userEvent));
        when(userEventRepository.save(any(UserEvent.class))).thenReturn(userEvent);

        UserEvent result = userEventService.markAttendance(10L, true);

        assertTrue(result.getAttended());
        verify(userEventRepository, times(1)).save(userEvent);
    }

    @Test
    void shouldThrowWhenUserEventNotFoundOnMarkAttendance() {
        when(userEventRepository.findById(404L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userEventService.markAttendance(404L, true)
        );
        assertEquals("UserEvent not found", ex.getMessage());
    }

    @Test
    void shouldCancelRegistrationAndSendNotification() {
        when(userEventRepository.findById(10L)).thenReturn(Optional.of(userEvent));

        userEventService.cancelRegistration(10L);

        verify(userEventRepository, times(1)).delete(userEvent);
        verify(notificationService, times(1))
                .sendNotification(eq(1L), contains("Has cancelado tu inscripción"), eq("USER_EVENT_DELETE"), eq(10));
    }

    @Test
    void shouldThrowWhenCancelingNonexistentRegistration() {
        when(userEventRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userEventService.cancelRegistration(999L)
        );
        assertEquals("Registration not found", ex.getMessage());
    }

    @Test
    void shouldReturnAllUserEvents() {
        when(userEventRepository.findAll()).thenReturn(List.of(userEvent));

        List<UserEvent> result = userEventService.getAllUserEvents();

        assertEquals(1, result.size());
        assertEquals(userEvent, result.get(0));
        verify(userEventRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserEventsByEvent() {
        when(eventRepository.findById(100L)).thenReturn(Optional.of(event));
        when(userEventRepository.findByEventId(100L)).thenReturn(List.of(userEvent));

        List<UserEvent> result = userEventService.getUserEventsByEvent(100L);

        assertEquals(1, result.size());
        assertEquals(userEvent, result.get(0));
        verify(eventRepository, times(1)).findById(100L);
        verify(userEventRepository, times(1)).findByEventId(100L);
    }

    @Test
    void shouldThrowWhenEventNotFoundInGetUserEventsByEvent() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userEventService.getUserEventsByEvent(999L)
        );

        assertEquals("Event not found", ex.getMessage());
    }


}

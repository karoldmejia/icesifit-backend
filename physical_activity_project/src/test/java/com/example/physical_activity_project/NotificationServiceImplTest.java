package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Notification;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.INotificationRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private INotificationRepository notificationRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Heiner");

        notification = new Notification();
        notification.setId(10L);
        notification.setUser(user);
        notification.setText("Hola");
        notification.setOriginType("EVENT");
        notification.setOriginId(100);
        notification.setReadFlag(false);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testSendNotification_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.sendNotification(1L, "Hola", "EVENT", 100);

        assertNotNull(result);
        assertEquals("Hola", result.getText());
        verify(notificationRepository).save(any(Notification.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/notifications/1"), eq(notification));
    }

    @Test
    void testSendNotification_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notificationService.sendNotification(1L, "Hola", "EVENT", 10));

        assertEquals("User not found", ex.getMessage());
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testGetNotificationsByUser_Success() {
        when(notificationRepository.findByUserIdOrderBySentDateDesc(1L)).thenReturn(List.of(notification));

        List<Notification> result = notificationService.getNotificationsByUser(1L);

        assertEquals(1, result.size());
        verify(notificationRepository).findByUserIdOrderBySentDateDesc(1L);
    }

    @Test
    void testMarkAsRead_Success() {
        notification.setReadFlag(false);
        when(notificationRepository.findById(10L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.markAsRead(10L);

        assertTrue(result.getReadFlag());
        verify(notificationRepository).save(notification);
    }

    @Test
    void testMarkAsRead_NotFound() {
        when(notificationRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notificationService.markAsRead(10L));

        assertEquals("Notification not found", ex.getMessage());
    }

    @Test
    void testSendNotificationToAll_Success() {
        User u1 = new User();
        u1.setId(1L);
        User u2 = new User();
        u2.setId(2L);
        List<User> users = List.of(u1, u2);

        Notification n1 = new Notification();
        n1.setUser(u1);
        Notification n2 = new Notification();
        n2.setUser(u2);

        when(userRepository.findAll()).thenReturn(users);
        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(n1)
                .thenReturn(n2);

        notificationService.sendNotificationToAll("Mensaje global", "BROADCAST", 50);

        verify(notificationRepository, times(2)).save(any(Notification.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/notifications/1"), any(Notification.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/notifications/2"), any(Notification.class));
    }
}

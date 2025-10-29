package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.Notification;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.INotificationRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.INotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;
    private final IUserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate; // WebSocket template

    public NotificationServiceImpl(INotificationRepository notificationRepository, IUserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Notification sendNotification(Long userId, String text, String originType, Integer originId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setText(text);
        notification.setOriginType(originType);
        notification.setOriginId(originId);
        notification.setReadFlag(false);
        notification.setSentDate(new Timestamp(System.currentTimeMillis()));

        Notification saved = notificationRepository.save(notification);

        // Enviar la notificación en tiempo real al canal del usuario
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, saved);

        return saved;
    }

    @Override
    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserIdOrderBySentDateDesc(userId);
    }

    @Override
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadFlag(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void sendNotificationToAll(String text, String originType, Integer originId) {
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setText(text);
            notification.setOriginType(originType);
            notification.setOriginId(originId);
            notification.setReadFlag(false);
            notification.setSentDate(new Timestamp(System.currentTimeMillis()));

            Notification saved = notificationRepository.save(notification);

            // Enviar la notificación a cada usuario conectado
            messagingTemplate.convertAndSend("/topic/notifications/" + user.getId(), saved);
        }
    }

    @Override
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }


}

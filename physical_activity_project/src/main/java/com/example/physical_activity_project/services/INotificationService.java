package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.Notification;

import java.util.List;

public interface INotificationService {
    Notification sendNotification(Long userId, String text, String originType, Integer originId);
    List<Notification> getNotificationsByUser(Long userId);
    Notification markAsRead(Long notificationId);
    void sendNotificationToAll(String text, String originType, Integer originId);
    void deleteNotification(Long notificationId);
    }

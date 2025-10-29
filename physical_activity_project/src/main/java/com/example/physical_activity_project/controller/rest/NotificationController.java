package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.NotificationDTO;
import com.example.physical_activity_project.dto.NotificationRequest;
import com.example.physical_activity_project.mappers.NotificationMapper;
import com.example.physical_activity_project.model.Notification;
import com.example.physical_activity_project.services.impl.NotificationServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
public class NotificationController {

    private final NotificationServiceImpl notificationService;
    private final NotificationMapper notificationMapper;

    @PostMapping("/user/{userId}")
    public ResponseEntity<NotificationDTO> sendNotification(
            @PathVariable Long userId,
            @RequestBody NotificationRequest request) {

        Notification notification = notificationService.sendNotification(
                userId,
                request.getText(),
                request.getOriginType(),
                request.getOriginId()
        );

        return ResponseEntity.ok(notificationMapper.entityToDto(notification));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUser(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUser(userId);
        List<NotificationDTO> notificationDTOs = notifications.stream()
                .map(notificationMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(notificationDTOs);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(notificationMapper.entityToDto(notification));
    }

    @PostMapping("/all")
    public ResponseEntity<Void> sendNotificationToAll(
            @RequestBody NotificationRequest request) {

        notificationService.sendNotificationToAll(
                request.getText(),
                request.getOriginType(),
                request.getOriginId()
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}

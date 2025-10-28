package com.example.physical_activity_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.physical_activity_project.model.Notification;

import java.util.List;

public interface INotificationRepository extends JpaRepository<Notification, Long>{
    List<Notification> findByUserIdOrderBySentDateDesc(Long userId);
}

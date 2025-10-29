package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.physical_activity_project.model.UserEvent;

import java.util.List;
import java.util.Optional;

public interface IUserEventRepository extends JpaRepository<UserEvent, Long> {
    List<UserEvent> findByUserId(Long userId);
    List<UserEvent> findByEventId(Long eventId);


}

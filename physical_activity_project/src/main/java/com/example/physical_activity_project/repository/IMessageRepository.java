package com.example.physical_activity_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.physical_activity_project.model.Message;

import java.util.List;

public interface IMessageRepository extends JpaRepository<Message, Long>{
    List<Message> findByTrainerIdAndUserIdOrderBySendDateAsc(Long trainerId, Long userId);
}

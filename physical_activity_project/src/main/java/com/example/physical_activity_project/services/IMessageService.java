package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.Message;

import java.util.List;

public interface IMessageService {
    Message sendMessage(Long trainerId, Long userId, String content);
    List<Message> getMessagesBetweenTrainerAndUser(Long trainerId, Long userId);
    void deleteMessage(Long messageId);
}

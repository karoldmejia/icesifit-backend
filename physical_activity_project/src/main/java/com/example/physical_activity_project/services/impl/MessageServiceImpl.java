package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.Message;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserTrainerAssignment;
import com.example.physical_activity_project.repository.IMessageRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.repository.IUserTrainerAssignmentRepository;
import com.example.physical_activity_project.services.IMessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService {

    private final IMessageRepository messageRepository;
    private final IUserRepository userRepository;
    private final IUserTrainerAssignmentRepository assignmentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageServiceImpl(IMessageRepository messageRepository,
                              IUserRepository userRepository,
                              IUserTrainerAssignmentRepository assignmentRepository,
                              SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Message sendMessage(Long trainerId, Long userId, String content) {
        // Validar que existan los usuarios
        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verificar si la relación entre ambos es válida
        UserTrainerAssignment assignment = assignmentRepository
                .findByTrainerIdAndUserIdAndStatus(trainerId, userId, "ACTIVE")
                .orElseThrow(() -> new RuntimeException("No active assignment between trainer and user"));

        // Si existe relación activa, se permite enviar mensaje
        Message message = new Message();
        message.setTrainer(trainer);
        message.setUser(user);
        message.setContent(content);
        message.setSendDate(new Timestamp(System.currentTimeMillis()));

        Message saved = messageRepository.save(message);

        // Enviar notificación por WebSocket al usuario
        messagingTemplate.convertAndSend("/topic/messages/" + userId, saved);

        return saved;
    }

    @Override
    public List<Message> getMessagesBetweenTrainerAndUser(Long trainerId, Long userId) {
        // Solo devolver mensajes si existe relación activa
        boolean hasAssignment = assignmentRepository
                .findByTrainerIdAndUserIdAndStatus(trainerId, userId, "ACTIVE")
                .isPresent();

        if (!hasAssignment) {
            throw new RuntimeException("Access denied: no active assignment between these users");
        }

        return messageRepository.findByTrainerIdAndUserIdOrderBySendDateAsc(trainerId, userId);
    }

    @Override
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}

package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Message;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserTrainerAssignment;
import com.example.physical_activity_project.repository.IMessageRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.repository.IUserTrainerAssignmentRepository;
import com.example.physical_activity_project.services.impl.MessageServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    private IMessageRepository messageRepository;
    private IUserRepository userRepository;
    private IUserTrainerAssignmentRepository assignmentRepository;
    private SimpMessagingTemplate messagingTemplate;
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        messageRepository = mock(IMessageRepository.class);
        userRepository = mock(IUserRepository.class);
        assignmentRepository = mock(IUserTrainerAssignmentRepository.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);

        messageService = new MessageServiceImpl(
                messageRepository, userRepository, assignmentRepository, messagingTemplate
        );
    }

    @Test
    void sendMessage_ShouldSaveMessageAndSendNotification() {
        Long trainerId = 1L;
        Long userId = 2L;
        String content = "Hola, cómo va tu progreso?";

        User trainer = new User();
        trainer.setId(trainerId);
        User user = new User();
        user.setId(userId);

        UserTrainerAssignment assignment = new UserTrainerAssignment();

        when(userRepository.findById(trainerId)).thenReturn(Optional.of(trainer));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(assignmentRepository.findByTrainerIdAndUserIdAndStatus(trainerId, userId, "ACTIVE"))
                .thenReturn(Optional.of(assignment));

        Message savedMessage = new Message();
        savedMessage.setId(10L);
        savedMessage.setTrainer(trainer);
        savedMessage.setUser(user);
        savedMessage.setContent(content);
        savedMessage.setSendDate(new Timestamp(System.currentTimeMillis()));

        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        Message result = messageService.sendMessage(trainerId, userId, content);

        assertNotNull(result);
        assertEquals(content, result.getContent());
        verify(messageRepository).save(any(Message.class));
        verify(messagingTemplate).convertAndSend("/topic/messages/" + userId, savedMessage);
    }

    @Test
    void sendMessage_ShouldThrow_WhenTrainerNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> messageService.sendMessage(1L, 2L, "Hola"));

        assertEquals("Trainer not found", ex.getMessage());
        verify(messageRepository, never()).save(any());
    }

    @Test
    void sendMessage_ShouldThrow_WhenUserNotFound() {
        User trainer = new User();
        trainer.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> messageService.sendMessage(1L, 2L, "Hola"));

        assertEquals("User not found", ex.getMessage());
        verify(messageRepository, never()).save(any());
    }

    @Test
    void sendMessage_ShouldThrow_WhenNoActiveAssignment() {
        User trainer = new User();
        trainer.setId(1L);
        User user = new User();
        user.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(assignmentRepository.findByTrainerIdAndUserIdAndStatus(1L, 2L, "ACTIVE"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> messageService.sendMessage(1L, 2L, "Hola"));

        assertEquals("No active assignment between trainer and user", ex.getMessage());
        verify(messageRepository, never()).save(any());
    }

    @Test
    void getMessagesBetweenTrainerAndUser_ShouldReturnMessages_WhenAssignmentExists() {
        Long trainerId = 1L;
        Long userId = 2L;

        when(assignmentRepository.findByTrainerIdAndUserIdAndStatus(trainerId, userId, "ACTIVE"))
                .thenReturn(Optional.of(new UserTrainerAssignment()));

        List<Message> messages = List.of(new Message(), new Message());
        when(messageRepository.findByTrainerIdAndUserIdOrderBySendDateAsc(trainerId, userId))
                .thenReturn(messages);

        List<Message> result = messageService.getMessagesBetweenTrainerAndUser(trainerId, userId);

        assertEquals(2, result.size());
        verify(messageRepository).findByTrainerIdAndUserIdOrderBySendDateAsc(trainerId, userId);
    }

    @Test
    void getMessagesBetweenTrainerAndUser_ShouldThrow_WhenNoActiveAssignment() {
        when(assignmentRepository.findByTrainerIdAndUserIdAndStatus(1L, 2L, "ACTIVE"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> messageService.getMessagesBetweenTrainerAndUser(1L, 2L));

        assertEquals("Access denied: no active assignment between these users", ex.getMessage());
        verify(messageRepository, never()).findByTrainerIdAndUserIdOrderBySendDateAsc(any(), any());
    }

    @Test
        void deleteMessage_ShouldCallRepositoryDeleteById() {
        Long messageId = 10L;
        messageService.deleteMessage(messageId);

        verify(messageRepository, times(1)).deleteById(messageId);
        }

}
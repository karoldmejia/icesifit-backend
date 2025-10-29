package com.example.physical_activity_project;

import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserTrainerAssignment;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.repository.IUserTrainerAssignmentRepository;
import com.example.physical_activity_project.services.impl.NotificationServiceImpl;
import com.example.physical_activity_project.services.impl.UserTrainerAssignmentServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTrainerAssignmentServiceImplTest {

    @Mock
    private IUserTrainerAssignmentRepository assignmentRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private NotificationServiceImpl notificationService;

    @InjectMocks
    private UserTrainerAssignmentServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service.notificationService = notificationService;
    }

    @Test
    void testAssignTrainerToUser_Success() {
        User trainer = new User();
        trainer.setId(1L);
        trainer.setName("Carlos");

        User user = new User();
        user.setId(2L);
        user.setName("Heiner");

        UserTrainerAssignment saved = new UserTrainerAssignment();
        saved.setId(100L);
        saved.setTrainer(trainer);
        saved.setUser(user);
        saved.setStatus("ACTIVE");
        saved.setAssignmentDate(new Timestamp(System.currentTimeMillis()));

        when(userRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(assignmentRepository.save(any(UserTrainerAssignment.class))).thenReturn(saved);

        UserTrainerAssignment result = service.assignTrainerToUser(1L, 2L);

        assertNotNull(result);
        assertEquals("ACTIVE", result.getStatus());
        verify(notificationService).sendNotification(
                eq(2L),
                contains("Carlos"),
                eq("TRAINER_ASSIGNMENT"),
                eq(100)
        );
    }

    @Test
    void testAssignTrainerToUser_TrainerNotFound_Throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.assignTrainerToUser(1L, 2L));

        assertEquals("Trainer not found", ex.getMessage());
    }

    @Test
    void testAssignTrainerToUser_UserNotFound_Throws() {
        User trainer = new User();
        trainer.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.assignTrainerToUser(1L, 2L));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testUpdateAssignmentStatus_Success() {
        User trainer = new User();
        trainer.setId(1L);
        trainer.setName("Carlos");

        User user = new User();
        user.setId(2L);

        UserTrainerAssignment existing = new UserTrainerAssignment();
        existing.setId(10L);
        existing.setTrainer(trainer);
        existing.setUser(user);
        existing.setStatus("ACTIVE");

        when(assignmentRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(assignmentRepository.save(any(UserTrainerAssignment.class))).thenReturn(existing);

        UserTrainerAssignment result = service.updateAssignmentStatus(10L, "INACTIVE");

        assertEquals("INACTIVE", result.getStatus());
        verify(notificationService).sendNotification(
                eq(2L),
                contains("Carlos"),
                eq("TRAINER_ASSIGNMENT"),
                eq(10)
        );
    }

    @Test
    void testUpdateAssignmentStatus_NotFound_Throws() {
        when(assignmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.updateAssignmentStatus(99L, "ACTIVE"));
    }

    @Test
    void testGetAssignmentsByTrainer_Success() {
        UserTrainerAssignment assignment = new UserTrainerAssignment();
        when(assignmentRepository.findByTrainerId(1L)).thenReturn(List.of(assignment));

        List<UserTrainerAssignment> result = service.getAssignmentsByTrainer(1L);

        assertEquals(1, result.size());
        verify(assignmentRepository).findByTrainerId(1L);
    }

    @Test
    void testGetAssignmentsByUser_Success() {
        UserTrainerAssignment assignment = new UserTrainerAssignment();
        when(assignmentRepository.findByUserId(2L)).thenReturn(List.of(assignment));

        List<UserTrainerAssignment> result = service.getAssignmentsByUser(2L);

        assertEquals(1, result.size());
        verify(assignmentRepository).findByUserId(2L);
    }

    @Test
    void testDeleteAssignment_Success() {
        User trainer = new User();
        trainer.setId(1L);
        trainer.setName("Carlos");

        User user = new User();
        user.setId(2L);

        UserTrainerAssignment existing = new UserTrainerAssignment();
        existing.setId(50L);
        existing.setTrainer(trainer);
        existing.setUser(user);

        when(assignmentRepository.findById(50L)).thenReturn(Optional.of(existing));

        service.deleteAssignment(50L);

        verify(assignmentRepository).deleteById(50L);
        verify(notificationService).sendNotification(
                eq(2L),
                contains("Carlos"),
                eq("TRAINER_ASSIGNMENT"),
                eq(50)
        );
    }

    @Test
    void testDeleteAssignment_NotFound_Throws() {
        when(assignmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.deleteAssignment(999L));
    }
}

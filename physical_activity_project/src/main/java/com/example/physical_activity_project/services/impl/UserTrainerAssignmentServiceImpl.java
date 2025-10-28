package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserTrainerAssignment;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.repository.IUserTrainerAssignmentRepository;
import com.example.physical_activity_project.services.IUserTrainerAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserTrainerAssignmentServiceImpl implements IUserTrainerAssignmentService {

    private final IUserTrainerAssignmentRepository assignmentRepository;
    private final IUserRepository userRepository;
    private NotificationServiceImpl notificationService;


    public UserTrainerAssignmentServiceImpl(IUserTrainerAssignmentRepository assignmentRepository, IUserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserTrainerAssignment assignTrainerToUser(Long trainerId, Long userId) {
        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserTrainerAssignment assignment = new UserTrainerAssignment();
        assignment.setTrainer(trainer);
        assignment.setUser(user);
        assignment.setStatus("ACTIVE");
        assignment.setAssignmentDate(new Timestamp(System.currentTimeMillis()));

        UserTrainerAssignment saved = assignmentRepository.save(assignment);
        notificationService.sendNotification(
                userId,
                "Has sido asignado a un nuevo entrenador: " + trainer.getName(),
                "TRAINER_ASSIGNMENT",
                Math.toIntExact(saved.getId())
        );

        return saved;
    }

    @Override
    public UserTrainerAssignment updateAssignmentStatus(Long assignmentId, String newStatus) {
        UserTrainerAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la asignación con id: " + assignmentId));
        assignment.setStatus(newStatus);
        UserTrainerAssignment saved = assignmentRepository.save(assignment);
        Long userId = assignment.getUser().getId();
        String trainerName = assignment.getTrainer().getName();
        notificationService.sendNotification(
                userId,
                "Tu asignación al entrenador " + trainerName + " ha sido actualizada a: " + newStatus,
                "TRAINER_ASSIGNMENT",
                Math.toIntExact(saved.getId())
        );
        return saved;
    }


    @Override
    public List<UserTrainerAssignment> getAssignmentsByTrainer(Long trainerId) {
        return assignmentRepository.findByTrainerId(trainerId);
    }

    @Override
    public List<UserTrainerAssignment> getAssignmentsByUser(Long userId) {
        return assignmentRepository.findByUserId(userId);
    }

    @Override
    public void deleteAssignment(Long assignmentId) {
        UserTrainerAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la asignación con id: " + assignmentId));
        Long userId = assignment.getUser().getId();
        String trainerName = assignment.getTrainer().getName();
        assignmentRepository.deleteById(assignmentId);
        notificationService.sendNotification(
                userId,
                "Tu asignación al entrenador " + trainerName + " ha sido eliminada",
                "TRAINER_ASSIGNMENT",
                Math.toIntExact(assignmentId)
        );
    }
}
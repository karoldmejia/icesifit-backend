package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.Recommendation;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.IExerciseProgressRepository;
import com.example.physical_activity_project.repository.IRecommendationRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.INotificationService;
import com.example.physical_activity_project.services.IRecommendationService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RecommendationServiceImpl implements IRecommendationService {

    private final IRecommendationRepository recommendationRepository;
    private final IUserRepository userRepository;
    private final IExerciseProgressRepository progressRepository;
    private final INotificationService notificationService;

    public RecommendationServiceImpl(
            IRecommendationRepository recommendationRepository,
            IUserRepository userRepository,
            IExerciseProgressRepository progressRepository,
            INotificationService notificationService
    ) {
        this.recommendationRepository = recommendationRepository;
        this.userRepository = userRepository;
        this.progressRepository = progressRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Recommendation createRecommendation(Long trainerId, Long progressId, String content) {
        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
        ExerciseProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new RuntimeException("Progress not found"));

        Recommendation recommendation = new Recommendation();
        recommendation.setTrainer(trainer);
        recommendation.setExerciseProgress(progress);
        recommendation.setContent(content);
        recommendation.setRecommendationDate(new Timestamp(System.currentTimeMillis()));
        recommendation.setStatus("NEW");

        Recommendation saved = recommendationRepository.save(recommendation);

        Long userId = progress.getUser().getId();
        notificationService.sendNotification(
                userId,
                "Tu entrenador " + trainer.getName() + " ha dejado una nueva recomendación sobre tu progreso.",
                "RECOMMENDATION_NEW",
                Math.toIntExact(saved.getId())
        );

        return saved;
    }

    @Override
    public List<Recommendation> getRecommendationsByTrainer(Long trainerId) {
        return recommendationRepository.findByTrainerId(trainerId);
    }

    @Override
    public List<Recommendation> getRecommendationsByUser(Long userId) {
        return recommendationRepository.findByExerciseProgress_UserId(userId);
    }

    @Override
    public Recommendation updateRecommendationStatus(Long recommendationId, String newStatus) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));

        recommendation.setStatus(newStatus);
        Recommendation saved = recommendationRepository.save(recommendation);

        Long trainerId = saved.getTrainer().getId();
        User user = saved.getExerciseProgress().getUser();

        notificationService.sendNotification(
                trainerId,
                "El estado de tu recomendación para " + user.getName() + " cambió a: " + newStatus,
                "RECOMMENDATION_STATUS",
                Math.toIntExact(saved.getId())
        );

        return saved;
    }
}

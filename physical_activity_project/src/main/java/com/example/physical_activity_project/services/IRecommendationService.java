package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.Recommendation;

import java.util.List;

public interface IRecommendationService {

    Recommendation createRecommendation(Long trainerId, Long progressId, String content);
    List<Recommendation> getRecommendationsByTrainer(Long trainerId);
    List<Recommendation> getRecommendationsByUser(Long userId);
    Recommendation updateRecommendationStatus(Long recommendationId, String newStatus);
    void deleteRecommendation(Long recommendationId);

}

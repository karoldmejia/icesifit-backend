package com.example.physical_activity_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.physical_activity_project.model.Recommendation;

import java.util.List;

public interface IRecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByTrainerId(Long trainerId);
    List<Recommendation> findByExerciseProgress_UserId(Long userId);
}

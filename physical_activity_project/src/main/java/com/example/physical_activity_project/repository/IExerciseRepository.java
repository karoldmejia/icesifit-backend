package com.example.physical_activity_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.physical_activity_project.model.Exercise;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByType(String type);
    List<Exercise> findByDifficulty(String difficulty);
}
package com.example.physical_activity_project;

import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.Recommendation;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.IExerciseProgressRepository;
import com.example.physical_activity_project.repository.IRecommendationRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.INotificationService;
import com.example.physical_activity_project.services.impl.RecommendationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommendationServiceImplTest {

    @Mock
    private IRecommendationRepository recommendationRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IExerciseProgressRepository progressRepository;

    @Mock
    private INotificationService notificationService;

    @InjectMocks
    private RecommendationServiceImpl service;

    private User trainer;
    private User user;
    private ExerciseProgress progress;
    private Recommendation recommendation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trainer = new User();
        trainer.setId(1L);
        trainer.setName("Coach Heiner");

        user = new User();
        user.setId(2L);
        user.setName("Estudiante");

        progress = new ExerciseProgress();
        progress.setId(100L);
        progress.setUser(user);

        recommendation = new Recommendation();
        recommendation.setId(200L);
        recommendation.setTrainer(trainer);
        recommendation.setExerciseProgress(progress);
        recommendation.setContent("Haz más flexiones");
        recommendation.setStatus("NEW");
        recommendation.setRecommendationDate(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testCreateRecommendation_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(progressRepository.findById(100L)).thenReturn(Optional.of(progress));
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(recommendation);

        Recommendation result = service.createRecommendation(1L, 100L, "Haz más flexiones");

        assertNotNull(result);
        assertEquals("NEW", result.getStatus());
        verify(recommendationRepository).save(any(Recommendation.class));
        verify(notificationService).sendNotification(
                eq(2L),
                contains("Coach Heiner"),
                eq("RECOMMENDATION_NEW"),
                anyInt()
        );
    }

    @Test
    void testCreateRecommendation_TrainerNotFound_Throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createRecommendation(1L, 100L, "Haz más flexiones"));

        assertEquals("Trainer not found", ex.getMessage());
        verify(recommendationRepository, never()).save(any());
    }
    @Test
    void testCreateRecommendation_ProgressNotFound_Throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(trainer));
        when(progressRepository.findById(100L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createRecommendation(1L, 100L, "Haz más flexiones"));

        assertEquals("Progress not found", ex.getMessage());
    }

    @Test
    void testGetRecommendationsByTrainer_Success() {
        when(recommendationRepository.findByTrainerId(1L)).thenReturn(List.of(recommendation));

        List<Recommendation> result = service.getRecommendationsByTrainer(1L);

        assertEquals(1, result.size());
        verify(recommendationRepository).findByTrainerId(1L);
    }

    @Test
    void testGetRecommendationsByUser_Success() {
        when(recommendationRepository.findByExerciseProgress_UserId(2L)).thenReturn(List.of(recommendation));

        List<Recommendation> result = service.getRecommendationsByUser(2L);

        assertEquals(1, result.size());
        verify(recommendationRepository).findByExerciseProgress_UserId(2L);
    }

    @Test
    void testUpdateRecommendationStatus_Success() {
        when(recommendationRepository.findById(200L)).thenReturn(Optional.of(recommendation));
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(recommendation);

        Recommendation result = service.updateRecommendationStatus(200L, "REVIEWED");

        assertEquals("REVIEWED", result.getStatus());
        verify(recommendationRepository).save(recommendation);
        verify(notificationService).sendNotification(
                eq(1L),
                contains("Estudiante"),
                eq("RECOMMENDATION_STATUS"),
                anyInt()
        );
    }

    @Test
    void testUpdateRecommendationStatus_NotFound_Throws() {
        when(recommendationRepository.findById(200L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateRecommendationStatus(200L, "REVIEWED"));

        assertEquals("Recommendation not found", ex.getMessage());
        verify(recommendationRepository, never()).save(any());
    }
}

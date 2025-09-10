package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IRecommendationRepository;
import com.example.physical_activity_project.services.IRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements IRecommendationService {

    private final IRecommendationRepository recommendationRepository;
    private final Logger logger = Logger.getLogger(RecommendationServiceImpl.class.getName());
}

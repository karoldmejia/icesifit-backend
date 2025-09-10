package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IExerciseProgressRepository;
import com.example.physical_activity_project.services.IExerciseProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ExerciseProgressServiceImpl implements IExerciseProgressService {
    private final IExerciseProgressRepository exerciseProgressRepository;
    private final Logger logger = Logger.getLogger(ExerciseProgressServiceImpl.class.getName());
}

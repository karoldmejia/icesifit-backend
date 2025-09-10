package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IExerciseRepository;
import com.example.physical_activity_project.services.IExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements IExerciseService {

    private final IExerciseRepository exerciseRepository;
    private final Logger logger = Logger.getLogger(ExerciseServiceImpl.class.getName());
}

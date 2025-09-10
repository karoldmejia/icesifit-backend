package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IRoutineExerciseRepository;
import com.example.physical_activity_project.services.IRoutineExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class RoutineExerciseServiceImpl implements IRoutineExerciseService {

    private final IRoutineExerciseRepository routineExerciseRepository;
    private final Logger logger = Logger.getLogger(RoutineExerciseServiceImpl.class.getName());
}

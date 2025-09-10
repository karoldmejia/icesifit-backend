package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IRoutineRepository;
import com.example.physical_activity_project.services.IRoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements IRoutineService {

    private final IRoutineRepository routineRepository;
    private final Logger logger = Logger.getLogger(RoutineServiceImpl.class.getName());
}

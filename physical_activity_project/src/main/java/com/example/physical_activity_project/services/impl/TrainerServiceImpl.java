package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.ITrainerRepository;
import com.example.physical_activity_project.services.ITrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements ITrainerService {

    private final ITrainerRepository trainerRepository;
    private final Logger logger = Logger.getLogger(TrainerServiceImpl.class.getName());
}

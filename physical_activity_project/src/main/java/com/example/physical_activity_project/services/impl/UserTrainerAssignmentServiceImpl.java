package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IUserTrainerAssignmentRepository;
import com.example.physical_activity_project.services.IUserTrainerAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserTrainerAssignmentServiceImpl implements IUserTrainerAssignmentService {

    private final IUserTrainerAssignmentRepository userTrainerAssignmentRepository;
    private final Logger logger = Logger.getLogger(UserTrainerAssignmentServiceImpl.class.getName());
}

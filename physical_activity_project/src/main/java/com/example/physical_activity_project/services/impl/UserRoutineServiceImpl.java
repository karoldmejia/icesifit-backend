package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IUserRoutineRepository;
import com.example.physical_activity_project.services.IUserRoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserRoutineServiceImpl implements IUserRoutineService {

    private final IUserRoutineRepository userRoutineRepository;
    private final Logger logger = Logger.getLogger(UserRoutineServiceImpl.class.getName());
}

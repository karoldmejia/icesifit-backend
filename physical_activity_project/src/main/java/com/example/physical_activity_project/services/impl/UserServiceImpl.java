package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
}

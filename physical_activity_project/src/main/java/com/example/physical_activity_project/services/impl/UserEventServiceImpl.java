package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IUserEventRepository;
import com.example.physical_activity_project.services.IUserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserEventServiceImpl implements IUserEventService {

    private final IUserEventRepository userEventRepository;
    private final Logger logger = Logger.getLogger(UserEventServiceImpl.class.getName());
}

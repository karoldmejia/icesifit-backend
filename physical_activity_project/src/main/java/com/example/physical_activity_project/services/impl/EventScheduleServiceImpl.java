package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IEventScheduleRepository;
import com.example.physical_activity_project.services.IEventScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class EventScheduleServiceImpl implements IEventScheduleService {

    private final IEventScheduleRepository eventScheduleRepository;
    private final Logger logger = Logger.getLogger(EventScheduleServiceImpl.class.getName());
}

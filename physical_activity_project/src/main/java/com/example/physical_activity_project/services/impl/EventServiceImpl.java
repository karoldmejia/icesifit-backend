package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IEventRepository;
import com.example.physical_activity_project.services.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {

    private final IEventRepository eventRepository;
    private final Logger logger = Logger.getLogger(EventServiceImpl.class.getName());
}

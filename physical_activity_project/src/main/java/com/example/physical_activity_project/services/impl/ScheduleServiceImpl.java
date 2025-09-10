package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IScheduleRepository;
import com.example.physical_activity_project.services.IScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements IScheduleService {

    private final IScheduleRepository scheduleRepository;
    private final Logger logger = Logger.getLogger(ScheduleServiceImpl.class.getName());
}

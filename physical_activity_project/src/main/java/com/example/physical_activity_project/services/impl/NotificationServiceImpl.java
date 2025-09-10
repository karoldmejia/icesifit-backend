package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.INotificationRepository;
import com.example.physical_activity_project.services.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;
    private final Logger logger = Logger.getLogger(NotificationServiceImpl.class.getName());
}

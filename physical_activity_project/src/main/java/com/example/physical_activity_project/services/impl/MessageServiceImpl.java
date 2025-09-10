package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.IMessageRepository;
import com.example.physical_activity_project.services.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final IMessageRepository messageRepository;
    private final Logger logger = Logger.getLogger(MessageServiceImpl.class.getName());
}

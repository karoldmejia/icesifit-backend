package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.repository.ISpaceRepository;
import com.example.physical_activity_project.services.ISpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class SpaceServiceImpl implements ISpaceService {

    private final ISpaceRepository spaceRepository;
    private final Logger logger = Logger.getLogger(SpaceServiceImpl.class.getName());
}

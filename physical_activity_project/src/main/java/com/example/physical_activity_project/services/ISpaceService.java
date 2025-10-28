package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.Space;

import java.util.List;

public interface ISpaceService {
    Space createSpace(Space space);
    List<Space> getAllSpaces();
    Space getSpaceById(Long id);
    Space updateSpace(Long id, Space updatedSpace);
    void deleteSpace(Long id);

}

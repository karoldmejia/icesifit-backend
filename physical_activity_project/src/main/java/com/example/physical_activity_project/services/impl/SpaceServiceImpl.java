package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.Space;
import com.example.physical_activity_project.repository.ISpaceRepository;
import com.example.physical_activity_project.services.INotificationService;
import com.example.physical_activity_project.services.ISpaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceServiceImpl implements ISpaceService {

    private final ISpaceRepository spaceRepository;
    private final INotificationService notificationService;

    public SpaceServiceImpl(ISpaceRepository spaceRepository,
                            INotificationService notificationService) {
        this.spaceRepository = spaceRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Space createSpace(Space space) {
        Space saved = spaceRepository.save(space);

        notificationService.sendNotificationToAll(
                "Nuevo espacio disponible: " + saved.getName(),
                "SPACE",
                Math.toIntExact(saved.getId())
        );
        return saved;
    }

    @Override
    public List<Space> getAllSpaces() {
        return spaceRepository.findAll();
    }

    @Override
    public Space getSpaceById(Long id) {
        return spaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Space not found"));
    }

    @Override
    public Space updateSpace(Long id, Space updatedSpace) {
        Space existing = getSpaceById(id);
        existing.setName(updatedSpace.getName());
        existing.setLocation(updatedSpace.getLocation());
        existing.setCapacity(updatedSpace.getCapacity());

        Space saved = spaceRepository.save(existing);

        notificationService.sendNotificationToAll(
                "El espacio '" + saved.getName() + "' ha sido actualizado.",
                "SPACE_UPDATE",
                Math.toIntExact(saved.getId())
        );
        return saved;
    }

    @Override
    public void deleteSpace(Long id) {
        Space existing = getSpaceById(id);
        spaceRepository.delete(existing);

        notificationService.sendNotification(
                0L,
                "El espacio '" + existing.getName() + "' ha sido eliminado.",
                "SPACE_DELETE",
                Math.toIntExact(id)
        );
    }
}

package com.example.physical_activity_project;

import com.example.physical_activity_project.model.Space;
import com.example.physical_activity_project.repository.ISpaceRepository;
import com.example.physical_activity_project.services.INotificationService;
import com.example.physical_activity_project.services.impl.SpaceServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SpaceServiceImplTest {

    private ISpaceRepository spaceRepository;
    private INotificationService notificationService;
    private SpaceServiceImpl spaceService;

    @BeforeEach
    void setUp() {
        spaceRepository = mock(ISpaceRepository.class);
        notificationService = mock(INotificationService.class);
        spaceService = new SpaceServiceImpl(spaceRepository, notificationService);
    }

    @Test
    void createSpace_ShouldSaveSpaceAndSendNotification() {
        Space space = new Space();
        space.setId(1L);
        space.setName("Cancha de fútbol");

        when(spaceRepository.save(space)).thenReturn(space);

        Space result = spaceService.createSpace(space);

        assertNotNull(result);
        assertEquals("Cancha de fútbol", result.getName());
        verify(spaceRepository).save(space);
        verify(notificationService).sendNotificationToAll(
                "Nuevo espacio disponible: Cancha de fútbol",
                "SPACE",
                1
        );
    }

    @Test
    void getAllSpaces_ShouldReturnListOfSpaces() {
        when(spaceRepository.findAll()).thenReturn(List.of(new Space(), new Space()));

        List<Space> result = spaceService.getAllSpaces();

        assertEquals(2, result.size());
        verify(spaceRepository).findAll();
    }

    @Test
    void getSpaceById_ShouldReturnSpace_WhenExists() {
        Space space = new Space();
        space.setId(1L);
        space.setName("Gimnasio");

        when(spaceRepository.findById(1L)).thenReturn(Optional.of(space));

        Space result = spaceService.getSpaceById(1L);

        assertNotNull(result);
        assertEquals("Gimnasio", result.getName());
        verify(spaceRepository).findById(1L);
    }

    @Test
    void getSpaceById_ShouldThrow_WhenNotFound() {
        when(spaceRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> spaceService.getSpaceById(1L));

        assertEquals("Space not found", ex.getMessage());
        verify(spaceRepository).findById(1L);
    }

    @Test
    void updateSpace_ShouldUpdateAndNotify() {
        Long id = 1L;

        Space existing = new Space();
        existing.setId(id);
        existing.setName("Cancha vieja");
        existing.setLocation("Bloque A");
        existing.setCapacity(20);

        Space updated = new Space();
        updated.setName("Cancha nueva");
        updated.setLocation("Bloque B");
        updated.setCapacity(25);

        when(spaceRepository.findById(id)).thenReturn(Optional.of(existing));
        when(spaceRepository.save(any(Space.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Space result = spaceService.updateSpace(id, updated);

        assertEquals("Cancha nueva", result.getName());
        assertEquals("Bloque B", result.getLocation());
        assertEquals(25, result.getCapacity());

        verify(spaceRepository).save(existing);
        verify(notificationService).sendNotificationToAll(
                "El espacio 'Cancha nueva' ha sido actualizado.",
                "SPACE_UPDATE",
                1
        );
    }

    @Test
    void updateSpace_ShouldThrow_WhenSpaceNotFound() {
        when(spaceRepository.findById(1L)).thenReturn(Optional.empty());

        Space updated = new Space();
        updated.setName("Piscina");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> spaceService.updateSpace(1L, updated));

        assertEquals("Space not found", ex.getMessage());
        verify(spaceRepository, never()).save(any());
    }

    @Test
    void deleteSpace_ShouldDeleteAndSendNotification() {
        Space space = new Space();
        space.setId(1L);
        space.setName("Pista de atletismo");

        when(spaceRepository.findById(1L)).thenReturn(Optional.of(space));

        spaceService.deleteSpace(1L);

        verify(spaceRepository).delete(space);
        verify(notificationService).sendNotificationToAll(
            "El espacio 'Pista de atletismo' ha sido eliminado.",
            "SPACE_DELETE",
            1
        );
    }

    @Test
    void deleteSpace_ShouldThrow_WhenNotFound() {
        when(spaceRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> spaceService.deleteSpace(1L));

        assertEquals("Space not found", ex.getMessage());
        verify(spaceRepository, never()).delete(any());
        verify(notificationService, never()).sendNotification(anyLong(), anyString(), anyString(), anyInt());
    }
}
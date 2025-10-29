package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.SpaceDTO;
import com.example.physical_activity_project.mappers.SpaceMapper;
import com.example.physical_activity_project.model.Space;
import com.example.physical_activity_project.services.ISpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final ISpaceService spaceService;
    private final SpaceMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREAR_ESPACIO')")
    public ResponseEntity<SpaceDTO> createSpace(@RequestBody SpaceDTO dto) {
        Space space = mapper.dtoToEntity(dto);
        Space saved = spaceService.createSpace(space);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VER_ESPACIOS')")
    public ResponseEntity<List<SpaceDTO>> getAllSpaces() {
        List<SpaceDTO> spaces = spaceService.getAllSpaces()
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VER_ESPACIOS')")
    public ResponseEntity<SpaceDTO> getSpaceById(@PathVariable Long id) {
        Space space = spaceService.getSpaceById(id);
        return ResponseEntity.ok(mapper.entityToDto(space));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDITAR_ESPACIO')")
    public ResponseEntity<SpaceDTO> updateSpace(@PathVariable Long id, @RequestBody SpaceDTO dto) {
        Space updated = mapper.dtoToEntity(dto);
        Space saved = spaceService.updateSpace(id, updated);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ELIMINAR_ESPACIO')")
    public ResponseEntity<Void> deleteSpace(@PathVariable Long id) {
        spaceService.deleteSpace(id);
        return ResponseEntity.ok().build();
    }
}

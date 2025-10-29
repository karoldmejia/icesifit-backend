package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.MessageDTO;
import com.example.physical_activity_project.mappers.MessageMapper;
import com.example.physical_activity_project.model.Message;
import com.example.physical_activity_project.services.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;
    private final MessageMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ENVIAR_MENSAJE')")
    public ResponseEntity<MessageDTO> sendMessage(
            @RequestParam Long trainerId,
            @RequestParam Long userId,
            @RequestParam String content
    ) {
        Message message = messageService.sendMessage(trainerId, userId, content);
        return ResponseEntity.ok(mapper.entityToDto(message));
    }

    @GetMapping("/conversation")
    @PreAuthorize("hasAuthority('VER_CONVERSACIONES_PROPIAS')")
    public ResponseEntity<List<MessageDTO>> getConversation(
            @RequestParam Long trainerId,
            @RequestParam Long userId
    ) {
        List<MessageDTO> messages = messageService.getMessagesBetweenTrainerAndUser(trainerId, userId)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ELIMINAR_MENSAJE_PROPIO')")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok("Message deleted successfully");
    }
}

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

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;
    private final MessageMapper mapper;

    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(
            @RequestParam Long trainerId,
            @RequestParam Long userId,
            @RequestParam String content
    ) {
        Message message = messageService.sendMessage(trainerId, userId, content);
        return ResponseEntity.ok(mapper.entityToDto(message));
    }

    @GetMapping("/conversation")
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
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok("Message deleted successfully");
    }
}

package com.ragul.ChatBot.controller;

import com.ragul.ChatBot.dto.ChatRequest;
import com.ragul.ChatBot.dto.ChatResponse;
import com.ragul.ChatBot.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request){
        String response = chatService.chat(request.getSiteId(), request.getConversationId(), request.getMessage());
        return ResponseEntity.ok(
                new ChatResponse(response)
        );
    }

}

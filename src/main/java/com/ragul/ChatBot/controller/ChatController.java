package com.ragul.ChatBot.controller;

import com.ragul.ChatBot.dto.ChatRequest;
import com.ragul.ChatBot.dto.ChatResponse;
import com.ragul.ChatBot.entity.Site;
import com.ragul.ChatBot.service.ChatService;
import com.ragul.ChatBot.service.SiteService;
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
    private final SiteService siteService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request){
        Site site = siteService.getSiteByPublicKey(
                        request.getPublicKey()
                );

        String response = chatService.chat(
                site.getSiteId(),
                request.getConversationId(),
                request.getMessage()
        );

        return ResponseEntity.ok(
                new ChatResponse(response)
        );
    }

}

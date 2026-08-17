package com.ragul.ChatBot.controller;

import com.ragul.ChatBot.dto.KnowledgeRequest;
import com.ragul.ChatBot.service.KnowledgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @PostMapping
    public ResponseEntity<Void> addKnowledge(@Valid @RequestBody KnowledgeRequest request){
        knowledgeService.addKnowledge(
                request.getSiteId(),
                request.getContent()
        );
        return ResponseEntity.ok().build();
    }

}

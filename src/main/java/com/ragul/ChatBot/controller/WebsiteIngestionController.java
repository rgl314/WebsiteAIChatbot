package com.ragul.ChatBot.controller;

import com.ragul.ChatBot.dto.WebsiteIngestionRequest;
import com.ragul.ChatBot.service.KnowledgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class WebsiteIngestionController {

    private final KnowledgeService knowledgeService;

    @PostMapping("/website")
    public ResponseEntity<String> ingestWebsite(
            @Valid @RequestBody WebsiteIngestionRequest request) {

        knowledgeService.ingestWebsite(
                request.getSiteId(),
                request.getUrl()
        );

        return ResponseEntity.ok(
                "Website ingested successfully"
        );
    }

    @GetMapping("/test-render")
    public ResponseEntity<String> testRender(
            @RequestParam String url) {

        return ResponseEntity.ok(
                knowledgeService.testWebsite(url)
        );
    }

}

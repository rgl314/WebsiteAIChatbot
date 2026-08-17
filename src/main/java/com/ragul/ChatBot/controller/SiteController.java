package com.ragul.ChatBot.controller;

import com.ragul.ChatBot.dto.IngestionResponse;
import com.ragul.ChatBot.dto.SiteRequest;
import com.ragul.ChatBot.dto.SiteResponse;
import com.ragul.ChatBot.entity.Site;
import com.ragul.ChatBot.service.KnowledgeService;
import com.ragul.ChatBot.service.SiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;
    private final KnowledgeService knowledgeService;

    @PostMapping
    public ResponseEntity<SiteResponse> createSite(@Valid @RequestBody SiteRequest request) {
        return ResponseEntity.ok(
                siteService.createSite(request)
        );
    }

    @PostMapping("/{siteId}/ingest")
    public ResponseEntity<IngestionResponse> ingest(
            @PathVariable String siteId) {

        Site site = siteService.getSite(siteId);

        IngestionResponse response =
                knowledgeService.ingestWebsite(
                        site.getSiteId(),
                        site.getDomain()
                );

        return ResponseEntity.ok(response);
    }

}

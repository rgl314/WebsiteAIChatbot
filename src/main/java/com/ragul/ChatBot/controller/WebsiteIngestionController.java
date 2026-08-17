package com.ragul.ChatBot.controller;

import com.ragul.ChatBot.dto.IngestionResponse;
import com.ragul.ChatBot.entity.Site;
import com.ragul.ChatBot.service.KnowledgeService;
import com.ragul.ChatBot.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
public class WebsiteIngestionController {

    private final KnowledgeService knowledgeService;
    private final SiteService siteService;

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

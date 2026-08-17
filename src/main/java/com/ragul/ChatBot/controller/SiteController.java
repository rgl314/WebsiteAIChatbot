package com.ragul.ChatBot.controller;

import com.ragul.ChatBot.dto.SiteRequest;
import com.ragul.ChatBot.dto.SiteResponse;
import com.ragul.ChatBot.service.SiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @PostMapping
    public ResponseEntity<SiteResponse> createSite(@Valid @RequestBody SiteRequest request) {
        return ResponseEntity.ok(
                siteService.createSite(request)
        );
    }

}

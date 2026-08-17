package com.ragul.ChatBot.service;

import com.ragul.ChatBot.dto.SiteRequest;
import com.ragul.ChatBot.dto.SiteResponse;
import com.ragul.ChatBot.entity.Site;
import com.ragul.ChatBot.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;

    public SiteResponse createSite(SiteRequest request){
        Site site = Site.builder()
                .siteId("site-" + UUID.randomUUID().toString().substring(0, 8))
                .name(request.getName())
                .domain(request.getDomain())
                .build();

        Site saved = siteRepository.save(site);

        return SiteResponse.builder()
                .siteId(saved.getSiteId())
                .name(saved.getName())
                .domain(saved.getDomain())
                .build();
    }

    public Site getSite(String siteId){
        return siteRepository.findBySiteId(siteId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Site not found: " + siteId
                ));
    }

}

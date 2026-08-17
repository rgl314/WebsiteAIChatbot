package com.ragul.ChatBot.service;

import com.ragul.ChatBot.entity.Site;
import com.ragul.ChatBot.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;

    public Site getSite(String siteId){
        return siteRepository.findBySiteId(siteId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Site not found: " + siteId
                ));
    }

}

package com.ragul.ChatBot.service;

import com.ragul.ChatBot.dto.SiteRequest;
import com.ragul.ChatBot.dto.SiteResponse;
import com.ragul.ChatBot.entity.Site;
import com.ragul.ChatBot.exception.SiteNotFoundException;
import com.ragul.ChatBot.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;

    public SiteResponse createSite(SiteRequest request){
        String siteId =
                "site-" + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8);

        String publicKey =
                "pk_" + UUID.randomUUID()
                        .toString()
                        .replace("-", "");

        Site site = Site.builder()
                .siteId(siteId)
                .publicKey(publicKey)
                .name(request.getName())
                .domain(request.getDomain())
                .build();

        Site saved = siteRepository.save(site);

        return SiteResponse.builder()
                .siteId(saved.getSiteId())
                .publicKey(saved.getPublicKey())
                .name(saved.getName())
                .domain(saved.getDomain())
                .build();
    }

    public Site getSite(String siteId){
        return siteRepository.findBySiteId(siteId)
                .orElseThrow(() -> new SiteNotFoundException(
                        "Site not found: " + siteId
                ));
    }

    public Site getSiteByPublicKey(String publicKey) {

        return siteRepository.findByPublicKey(publicKey)
                .orElseThrow(() ->
                        new SiteNotFoundException(
                                "Invalid site key = "+ publicKey +", Site not found!"
                        )
                );
    }

}

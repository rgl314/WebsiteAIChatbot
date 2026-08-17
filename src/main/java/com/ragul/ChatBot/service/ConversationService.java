package com.ragul.ChatBot.service;

import com.ragul.ChatBot.entity.Conversation;
import com.ragul.ChatBot.entity.Site;
import com.ragul.ChatBot.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final SiteService siteService;

    public Conversation getOrCreate(String siteId, String conversationId){

        return conversationRepository.findBySite_SiteIdAndConversationId(siteId, conversationId)
                .orElseGet(() -> {
                    Site site = siteService.getSite(siteId);
                    Conversation conversation = Conversation.builder()
                            .site(site)
                            .conversationId(conversationId)
                            .build();
                    return conversationRepository.save(conversation);
                });
    }

}

package com.ragul.ChatBot.repository;

import com.ragul.ChatBot.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findBySite_SiteIdAndConversationId(String siteId, String conversationId);

}

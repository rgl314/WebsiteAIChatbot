package com.ragul.ChatBot.service;

import com.ragul.ChatBot.entity.Site;
import com.ragul.ChatBot.util.ConversationKeyUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final SiteService siteService;
    private final ConversationService conversationService;

    public ChatService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, SiteService siteService, VectorStore vectorStore, ConversationService conversationService) {
        this.siteService = siteService;
        this.conversationService = conversationService;
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                You are a helpful website assistant.
        
                You have access to two sources of information:
        
                1. Conversation history:
                   Use it to understand references and facts previously
                   provided by the user during this conversation.
        
                2. Website knowledge:
                   Use it to answer questions about the website.
        
                Use conversation history for personal conversational facts
                such as the user's name.
        
                Use website knowledge for website-specific information.
        
                Never invent website-specific facts that are not present
                in the website knowledge.
                """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore).build()
                )
                .build();
    }

    public String chat(String siteId, String conversationId, String message){

        Site site = siteService.getSite(siteId);

        conversationService.getOrCreate(
                siteId,
                conversationId
        );

        String filterExpression = "siteId == '" + siteId + "'";

        String memoryConversationId =
                ConversationKeyUtil.create(
                        siteId,
                        conversationId
                );

        System.out.println(
                "Original conversationId: " + conversationId
        );

        System.out.println(
                "Memory conversationId: " + memoryConversationId
        );

        System.out.println(
                "Memory ID length: " + memoryConversationId.length()
        );

        return chatClient.prompt()
                .system("""
                        You are the assistant for the website: %s.

                        Use conversation history for conversational
                        context.

                        Use retrieved website knowledge for questions
                        about the website.

                        Never invent website-specific facts.
                        """.formatted(site.getName()))
                .advisors(advisor -> advisor
                                .param(
                                        ChatMemory.CONVERSATION_ID,
                                        memoryConversationId
                                ).param(
                                        QuestionAnswerAdvisor.FILTER_EXPRESSION,
                                        filterExpression
                                ))
                            .user(message)
                            .call()
                            .content();
    }

}

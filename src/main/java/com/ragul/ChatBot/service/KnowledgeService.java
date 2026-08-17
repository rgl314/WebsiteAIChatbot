package com.ragul.ChatBot.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KnowledgeService {

    private final VectorStore vectorStore;
    private final TokenTextSplitter textSplitter;

    public KnowledgeService(VectorStore vectorStore) {

        this.vectorStore = vectorStore;

        this.textSplitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(350)
                .withMinChunkLengthToEmbed(10)
                .build();
    }

    public void addKnowledge(String siteId, String content){

        Document document = new Document(
                content,
                Map.of("siteId", siteId)
        );

        List<Document> chunks = textSplitter.apply(List.of(document));
        vectorStore.add(chunks);
    }

}

package com.ragul.ChatBot.service;

import com.ragul.ChatBot.dto.IngestionResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class KnowledgeService {

    private final VectorStore vectorStore;
    private final TokenTextSplitter textSplitter;
    private final BrowserWebsiteLoader browserWebsiteLoader;

    public KnowledgeService(VectorStore vectorStore, BrowserWebsiteLoader browserWebsiteLoader) {

        this.vectorStore = vectorStore;
        this.browserWebsiteLoader = browserWebsiteLoader;
        this.textSplitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(350)
                .withMinChunkLengthToEmbed(10)
                .build();
    }

    public void addKnowledge(
            String siteId,
            String content) {

        Document document = new Document(
                content,
                Map.of("siteId", siteId)
        );

        List<Document> chunks =
                textSplitter.apply(List.of(document));

        vectorStore.add(chunks);
    }

    public IngestionResponse ingestWebsite(
            String siteId,
            String url) {

        try {

            System.out.println("Starting ingestion: " + url);

            String text =
                    browserWebsiteLoader.loadRenderedText(url);

            System.out.println(
                    "Rendered text length: " + text.length()
            );

            if (text == null || text.isBlank()) {
                throw new IllegalArgumentException(
                        "No readable content found at: " + url
                );
            }

            Document document = new Document(
                    text,
                    Map.of("siteId", siteId)
            );

            List<Document> chunks =
                    textSplitter.apply(List.of(document));

            System.out.println(
                    "Chunks created: " + chunks.size()
            );

            if (chunks.isEmpty()) {
                throw new IllegalArgumentException(
                        "No chunks were created."
                );
            }

            vectorStore.add(chunks);

            System.out.println(
                    "Successfully stored chunks in Qdrant."
            );

            return new IngestionResponse(
                    siteId,
                    1,
                    1,
                    chunks.size(),
                    0,
                    "COMPLETED"
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Website ingestion failed for: " + url,
                    e
            );
        }
    }

}
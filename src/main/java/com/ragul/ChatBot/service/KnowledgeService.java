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

            String text =
                    browserWebsiteLoader.loadRenderedText(url);

            if (text == null || text.isBlank()) {
                return new IngestionResponse(
                        siteId,
                        1,
                        0,
                        0,
                        1,
                        "FAILED"
                );
            }

            Document document = new Document(
                    text,
                    Map.of("siteId", siteId)
            );

            List<Document> chunks =
                    textSplitter.apply(List.of(document));

            if (chunks.isEmpty()) {
                return new IngestionResponse(
                        siteId,
                        1,
                        0,
                        0,
                        1,
                        "FAILED"
                );
            }

            vectorStore.add(chunks);

            return new IngestionResponse(
                    siteId,
                    1,
                    1,
                    chunks.size(),
                    0,
                    "COMPLETED"
            );

        } catch (Exception e) {

            return new IngestionResponse(
                    siteId,
                    1,
                    0,
                    0,
                    1,
                    "FAILED"
            );
        }
    }

    public String testWebsite(String url) {

        String text =
                browserWebsiteLoader.loadRenderedText(url);

        System.out.println("Rendered text length: " + text.length());

        System.out.println(
                text.substring(
                        0,
                        Math.min(text.length(), 3000)
                )
        );

        return text;
    }

    public String fetchHtml(String url) {

        RestClient client = RestClient.create();

        return client
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
}
package com.ragul.ChatBot.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngestionResponse {

    private String siteId;
    private int pagesDiscovered;
    private int pagesIndexed;
    private int chunksCreated;
    private int failedPages;
    private String status;

}

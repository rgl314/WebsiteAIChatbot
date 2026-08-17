package com.ragul.ChatBot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KnowledgeRequest {

    @NotBlank
    private String siteId;

    @NotBlank
    private String content;

}

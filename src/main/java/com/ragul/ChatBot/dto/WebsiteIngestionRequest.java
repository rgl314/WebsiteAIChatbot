package com.ragul.ChatBot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteIngestionRequest {

    @NotBlank
    private String siteId;

    @NotBlank
    @Pattern(
            regexp = "https?://.*",
            message = "URL must start with http:// or https://"
    )
    private String url;

}

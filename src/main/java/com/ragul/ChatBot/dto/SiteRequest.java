package com.ragul.ChatBot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(
            regexp = "https?://.*",
            message = "Domain must start with http:// or https://"
    )
    private String domain;

}

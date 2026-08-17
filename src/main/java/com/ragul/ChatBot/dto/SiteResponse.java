package com.ragul.ChatBot.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteResponse {

    private String siteId;
    private String publicKey;
    private String name;
    private String domain;

}

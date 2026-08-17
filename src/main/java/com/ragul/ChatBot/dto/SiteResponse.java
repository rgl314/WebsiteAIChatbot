package com.ragul.ChatBot.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteResponse {

    private String siteId;
    private String name;
    private String domain;

}

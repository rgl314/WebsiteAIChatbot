package com.ragul.ChatBot.config;

import com.ragul.ChatBot.security.SiteOriginValidationFilter;
import com.ragul.ChatBot.service.SiteService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityFilterConfig {

    @Bean
    public FilterRegistrationBean<SiteOriginValidationFilter> siteOriginValidationFilter(SiteService siteService) {

        FilterRegistrationBean<SiteOriginValidationFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(
                new SiteOriginValidationFilter(
                        siteService
                )
        );

        registration.addUrlPatterns(
                "/api/chat"
        );

        registration.setOrder(
                10
        );

        return registration;
    }

}

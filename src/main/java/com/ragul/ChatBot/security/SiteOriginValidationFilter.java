package com.ragul.ChatBot.security;

import com.ragul.ChatBot.entity.Site;
import com.ragul.ChatBot.service.SiteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
public class SiteOriginValidationFilter extends OncePerRequestFilter {

    private static final String PUBLIC_KEY_HEADER = "X-Chatbot-Public-Key";

    private final SiteService siteService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        boolean protectedEndpoint = path.equals("/api/chat");

        if(!protectedEndpoint){
            filterChain.doFilter(request, response);
            return;
        }

        if ("OPTIONS".equalsIgnoreCase(
                request.getMethod())) {

            filterChain.doFilter(
                    request,
                    response
            );
            return;
        }

        String origin = request.getHeader("Origin");

        String publicKey = request.getHeader(PUBLIC_KEY_HEADER);

        if(origin == null || origin.isBlank()){
            reject(
                    response,
                    "Missing Origin Header."
            );
            return;
        }

        if(publicKey == null || publicKey.isBlank()){
            reject(
                    response,
                    "Missing chatbot public key."
            );
            return;
        }

        Site site;

        try{
            site = siteService.getSiteByPublicKey(publicKey);
        }
        catch (Exception ex){
            reject(
                    response,
                    "Invalid chatbot public key."
            );
            return;
        }

        System.out.println(
                "========== ORIGIN VALIDATION =========="
        );

        System.out.println(
                "Request Origin: " + origin
        );

        System.out.println(
                "Public Key: " + publicKey
        );

        System.out.println(
                "Site ID: " + site.getSiteId()
        );

        System.out.println(
                "Registered Domain: " + site.getDomain()
        );

        System.out.println(
                "Origin Allowed: " +
                        isOriginAllowed(
                                origin,
                                site.getDomain()
                        )
        );

        if(!isOriginAllowed(origin, site.getDomain())){
            reject(
                    response,
                    "This website is not authorized to use this chatbot."
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isOriginAllowed(String requestOrigin, String registeredDomain){
        try{
            URI requestUri = URI.create(requestOrigin);

            URI registeredUri = URI.create(registeredDomain);

            return sameOrigin(requestUri, registeredUri);
        }
        catch (Exception e) {
            return false;
        }
    }

    private boolean sameOrigin(URI first, URI second){
        int firstPort = effectivePort(first);

        int secondPort = effectivePort(second);

        return first.getScheme()
                .equalsIgnoreCase(
                        second.getScheme()
                )

                && first.getHost()
                .equalsIgnoreCase(
                        second.getHost()
                )

                && firstPort == secondPort;
    }

    private int effectivePort(URI uri) {

        if (uri.getPort() != -1) {
            return uri.getPort();
        }

        if ("https".equalsIgnoreCase(
                uri.getScheme())) {

            return 443;
        }

        if ("http".equalsIgnoreCase(
                uri.getScheme())) {

            return 80;
        }

        return -1;
    }

    private void reject(
            HttpServletResponse response,
            String message)
            throws IOException {

        response.setStatus(
                HttpServletResponse.SC_FORBIDDEN
        );

        response.setContentType(
                "application/json"
        );

        response.getWriter().write(
                """
                {
                  "error": "ORIGIN_NOT_ALLOWED",
                  "message": "%s"
                }
                """.formatted(message)
        );
    }

}

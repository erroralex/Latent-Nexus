package com.nilsson.latentnexus.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for setting up and providing `WebClient` instances for external API calls.
 * <p>
 * This class defines two distinct `WebClient` beans, specifically tailored for interacting
 * with the Gemini API's text generation and embedding endpoints. By using separate `WebClient`
 * instances, it allows for independent configuration (e.g., base URLs, headers) and
 * clearer separation of concerns for different external services.
 * </p>
 * <p>
 * The base URLs for the Gemini API endpoints are injected from application properties,
 * promoting flexibility and easy environment-specific configuration. Both `WebClient` instances
 * are configured with `Content-Type: application/json` as a default header, suitable for RESTful
 * JSON-based interactions.
 * </p>
 * <p>
 * The `@Qualifier` annotations ensure that when these `WebClient` instances are injected
 * into other components (e.g., `GeminiOrchestrator`), the correct instance is provided
 * based on its unique qualifier name.
 * </p>
 */
@Configuration
public class WebClientConfig {

    @Value("${gemini.api.url.text}")
    private String geminiTextUrl;

    @Value("${gemini.api.url.embedding}")
    private String geminiEmbeddingUrl;

    @Bean
    @Qualifier("geminiTextWebClient")
    public WebClient geminiTextWebClient() {
        return WebClient.builder()
                .baseUrl(geminiTextUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    @Qualifier("geminiEmbeddingWebClient")
    public WebClient geminiEmbeddingWebClient() {
        return WebClient.builder()
                .baseUrl(geminiEmbeddingUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}

package com.nilsson.latentnexus.service;

import com.nilsson.latentnexus.domain.entity.AssetEntity;
import com.nilsson.latentnexus.dto.gemini.GeminiEmbeddingRequest;
import com.nilsson.latentnexus.dto.gemini.GeminiEmbeddingResponse;
import com.nilsson.latentnexus.dto.gemini.GeminiRequest;
import com.nilsson.latentnexus.dto.gemini.GeminiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

/**
 * Orchestrator service for interacting with the Google Gemini AI API.
 */
@Service
public class GeminiOrchestrator {
    private static final Logger log = LoggerFactory.getLogger(GeminiOrchestrator.class);

    private final WebClient textWebClient;
    private final WebClient embeddingWebClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_PROMPT_TEMPLATE = "Analyze this AI generation prompt and provide a 1-sentence description and 5 keywords: %s";

    public GeminiOrchestrator(@Qualifier("geminiTextWebClient") WebClient textWebClient,
                              @Qualifier("geminiEmbeddingWebClient") WebClient embeddingWebClient) {
        this.textWebClient = textWebClient;
        this.embeddingWebClient = embeddingWebClient;
    }

    public void enrichAssetMetadata(AssetEntity asset) {
        Map<String, Object> metadata = asset.getGenerationMetadata();
        if (metadata == null || metadata.isEmpty()) {
            return;
        }

        String rawPrompt = (String) metadata.getOrDefault("prompt", "Unknown AI Generation");

        Mono.zip(
                callTextAPI(rawPrompt).subscribeOn(Schedulers.boundedElastic()),
                callEmbeddingAPI(rawPrompt).subscribeOn(Schedulers.boundedElastic())
        ).doOnSuccess(tuple -> {
            metadata.put("ai_description", tuple.getT1());
            asset.setGenerationMetadata(metadata);
            asset.setEmbedding(tuple.getT2());
            log.info("Successfully enriched asset {} with Gemini AI data", asset.getFilename());
        }).doOnError(e -> {
            if (e instanceof WebClientResponseException we) {
                log.error("Gemini API error ({}): {}", we.getStatusCode(), we.getResponseBodyAsString());
            } else {
                log.error("Failed to enrich asset {}: {}", asset.getFilename(), e.getMessage());
            }
        }).block();
    }

    private Mono<String> callTextAPI(String prompt) {
        return textWebClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                .bodyValue(GeminiRequest.fromText(String.format(GEMINI_PROMPT_TEMPLATE, prompt)))
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .map(GeminiResponse::getFirstText);
    }

    private Mono<List<Float>> callEmbeddingAPI(String text) {
        return embeddingWebClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                .bodyValue(GeminiEmbeddingRequest.fromText(text))
                .retrieve()
                .bodyToMono(GeminiEmbeddingResponse.class)
                .map(response -> response.embedding().values());
    }
}

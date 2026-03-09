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
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

/**
 * Orchestrator service for interacting with the Google Gemini AI API.
 * <p>
 * This service manages the communication with Gemini's REST endpoints to perform semantic
 * enrichment of AI-generated assets. It leverages Spring WebClient for non-blocking I/O
 * and Project Reactor for managing asynchronous API calls.
 * </p>
 * <p>
 * Key functionalities include:
 * <ul>
 *     <li>Generating concise AI descriptions and keywords from raw generation prompts.</li>
 *     <li>Retrieving high-dimensional vector embeddings for assets to enable semantic search.</li>
 *     <li>Parallelizing API requests to minimize latency during the asset ingestion pipeline.</li>
 * </ul>
 * The service uses separate WebClient instances for text generation and embedding generation,
 * ensuring clean separation of concerns and independent configuration.
 * </p>
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

        // Asynchronously call Gemini for description and embedding, then join the results.
        Mono.zip(
                callTextAPI(rawPrompt).subscribeOn(Schedulers.boundedElastic()),
                callEmbeddingAPI(rawPrompt).subscribeOn(Schedulers.boundedElastic())
        ).doOnSuccess(tuple -> {
            String aiDescription = tuple.getT1();
            List<Float> embedding = tuple.getT2();

            metadata.put("ai_description", aiDescription);
            asset.setGenerationMetadata(metadata);
            asset.setEmbedding(embedding);

            log.info("Successfully enriched asset {} with Gemini AI description and embedding", asset.getFilename());
        }).doOnError(e -> {
            log.error("Failed to enrich asset {} with Gemini data: {}", asset.getFilename(), e.getMessage());
        }).block(); // Block to fit into the existing synchronous workflow.
    }

    private Mono<String> callTextAPI(String prompt) {
        String formattedPrompt = String.format(GEMINI_PROMPT_TEMPLATE, prompt);
        GeminiRequest requestBody = GeminiRequest.fromText(formattedPrompt);

        return textWebClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .map(GeminiResponse::getFirstText)
                .doOnError(e -> log.error("Gemini Text API call failed: {}", e.getMessage()));
    }

    private Mono<List<Float>> callEmbeddingAPI(String text) {
        GeminiEmbeddingRequest requestBody = GeminiEmbeddingRequest.fromText(text);

        return embeddingWebClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GeminiEmbeddingResponse.class)
                .map(response -> response.embedding().values())
                .doOnError(e -> log.error("Gemini Embedding API call failed: {}", e.getMessage()));
    }
}

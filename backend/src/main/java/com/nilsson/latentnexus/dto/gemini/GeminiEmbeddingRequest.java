package com.nilsson.latentnexus.dto.gemini;

import java.util.List;

/**
 * Represents the request body structure for interacting with the Gemini API's embedding models.
 * <p>
 * This record defines the JSON payload required by the Gemini Embedding API to generate
 * vector embeddings for a given text input. It encapsulates the `content` of the request,
 * which is a single `Content` object. This `Content` object, in turn, contains a list
 * of `Part` objects, where each `Part` holds the actual text for which an embedding
 * is to be generated.
 * </p>
 * <p>
 * The nested records (`Content` and `Part`) mirror the hierarchical structure expected
 * by the Gemini Embedding API.
 * </p>
 */
public record GeminiEmbeddingRequest(Content content) {

    public record Content(List<Part> parts) {
    }

    public record Part(String text) {
    }

    public static GeminiEmbeddingRequest fromText(String text) {
        return new GeminiEmbeddingRequest(new Content(List.of(new Part(text))));
    }
}

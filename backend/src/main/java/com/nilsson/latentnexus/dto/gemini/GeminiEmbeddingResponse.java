package com.nilsson.latentnexus.dto.gemini;

import java.util.List;

/**
 * Represents the response body structure received from the Gemini API's embedding models.
 * <p>
 * This record defines the JSON payload returned by the Gemini Embedding API after processing
 * an embedding generation request. It encapsulates a single {@link Embedding} object,
 * which contains the generated vector embedding.
 * </p>
 * <p>
 * The nested `Embedding` record mirrors the structure of the embedding data provided
 * by the Gemini API, allowing for direct deserialization of the vector values.
 * </p>
 */
public record GeminiEmbeddingResponse(Embedding embedding) {

    public record Embedding(List<Float> values) {
    }
}

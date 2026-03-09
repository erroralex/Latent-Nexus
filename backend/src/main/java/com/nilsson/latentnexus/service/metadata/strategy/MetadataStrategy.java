package com.nilsson.latentnexus.service.metadata.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

/**
 * Strategy interface for extracting and normalizing metadata from diverse AI generation tools.
 * <p>
 * Implementations of this interface provide logic for parsing raw metadata strings
 * or extracting specific fields from a JSON structure. This allows the application
 * to support multiple AI image generation formats (e.g., ComfyUI, Automatic1111, NovelAI)
 * in a modular and extensible way.
 * </p>
 */
public interface MetadataStrategy {

    /**
     * Extracts metadata from a specific JSON key-value pair.
     *
     * @param key the JSON key
     * @param value the JSON value
     * @param parentNode the parent JSON node
     * @param results the map to store extracted metadata
     */
    void extract(String key, JsonNode value, JsonNode parentNode, Map<String, Object> results);

    /**
     * Parses a raw metadata string into a map of key-value pairs.
     *
     * @param rawMetadata the raw metadata string
     * @return a map containing the extracted metadata
     */
    default Map<String, Object> parse(String rawMetadata) {
        return Map.of();
    }
}

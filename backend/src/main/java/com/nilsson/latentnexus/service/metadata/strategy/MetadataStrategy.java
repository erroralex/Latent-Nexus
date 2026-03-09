package com.nilsson.latentnexus.service.metadata.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Strategy interface for extracting and parsing AI generation metadata from various formats.
 * <p>
 * This interface defines the contract for specialized parsing strategies that handle
 * different AI image generation tools (e.g., ComfyUI, SwarmUI, InvokeAI). It supports
 * both incremental extraction from JSON nodes and direct parsing of raw metadata strings.
 * </p>
 * <p>
 * Implementing classes are responsible for identifying tool-specific keys and values
 * and normalizing them into a consistent map structure used by the application.
 * </p>
 */
public interface MetadataStrategy {
    void extract(String key, JsonNode value, JsonNode parentNode, Map<String, Object> results);

    default Map<String, Object> parse(String text, ObjectMapper mapper) {
        throw new UnsupportedOperationException("This strategy does not support direct text parsing.");
    }
}

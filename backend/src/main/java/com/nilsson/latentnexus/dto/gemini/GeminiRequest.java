package com.nilsson.latentnexus.dto.gemini;

import java.util.List;

/**
 * Represents the request body structure for interacting with the Gemini API's text generation models.
 * <p>
 * This record defines the JSON payload required by the Gemini API for generating content based on
 * a given prompt. It encapsulates the `contents` of the request, which is a list of `Content` objects.
 * Each `Content` object, in turn, contains a list of `Part` objects, where each `Part` typically
 * holds the actual text of the prompt.
 * </p>
 * <p>
 * The nested records (`Content` and `Part`) mirror the hierarchical structure expected by the
 * Gemini API for multi-part content generation.
 * </p>
 */
public record GeminiRequest(List<Content> contents) {

    public record Content(List<Part> parts) {
    }

    public record Part(String text) {
    }

    public static GeminiRequest fromText(String text) {
        return new GeminiRequest(List.of(new Content(List.of(new Part(text)))));
    }
}

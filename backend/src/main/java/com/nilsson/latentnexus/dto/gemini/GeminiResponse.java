package com.nilsson.latentnexus.dto.gemini;

import java.util.List;

/**
 * Represents the response body structure received from the Gemini API's text generation models.
 * <p>
 * This record defines the JSON payload returned by the Gemini API after processing a content
 * generation request. It encapsulates a list of `Candidate` objects, each representing a
 * potential generated response.
 * </p>
 * <p>
 * The nested records (`Candidate`, `Content`, and `Part`) mirror the hierarchical structure
 * of the Gemini API's response, allowing for easy deserialization of the generated text.
 * </p>
 */
public record GeminiResponse(List<Candidate> candidates) {

    public record Candidate(Content content) {
    }

    public record Content(List<Part> parts) {
    }

    public record Part(String text) {
    }

    public String getFirstText() {
        if (candidates != null && !candidates.isEmpty()) {
            Candidate firstCandidate = candidates.getFirst();
            if (firstCandidate.content() != null && firstCandidate.content().parts() != null && !firstCandidate.content().parts().isEmpty()) {
                return firstCandidate.content().parts().getFirst().text();
            }
        }
        return "No AI description generated.";
    }
}

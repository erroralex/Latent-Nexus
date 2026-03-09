package com.nilsson.latentnexus.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing an AI-generated asset.
 * <p>
 * This record serves as the public-facing contract for asset data exposed via the REST API.
 * It is designed to be immutable and contains a curated set of information derived from
 * the internal {@link com.nilsson.latentnexus.domain.entity.AssetEntity}. The use of a DTO
 * decouples the API's data structure from the persistence layer's entity model,
 * providing flexibility for future changes without impacting API consumers.
 * </p>
 * <p>
 * Key fields include basic file information, image dimensions, and a map for
 * AI generation metadata, which can contain details like prompts, seeds, and sampler settings.
 * The `workspaceId` provides context for multi-tenancy.
 * </p>
 */
public record AssetDTO(
        UUID id,
        String filename,
        @JsonProperty("mime_type") String mimeType,
        @JsonProperty("file_size_bytes") Long fileSizeBytes,
        Integer width,
        Integer height,
        @JsonProperty("generation_metadata") Map<String, Object> generationMetadata,
        @JsonProperty("workspace_id") UUID workspaceId
) {
}

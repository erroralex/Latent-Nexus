package com.nilsson.latentnexus.service.metadata;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nilsson.latentnexus.service.metadata.strategy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Orchestration service for parsing text-based image generation metadata into structured data.
 * <p>
 * This service identifies the format of the metadata (e.g., JSON, plain text) and selects
 * the appropriate parsing strategy (ComfyUI, SwarmUI, InvokeAI, NovelAI, or Common).
 * It handles both structured JSON and unstructured text formats commonly used by
 * various AI image generation tools.
 * </p>
 * <p>
 * The parser uses a strategy-based approach to extract key generation parameters such as
 * prompts, seeds, samplers, and model information, normalizing them into a consistent
 * map structure for storage and search.
 * </p>
 */
@Service
public class TextParamsParser {

    private static final Logger logger = LoggerFactory.getLogger(TextParamsParser.class);
    private static final ObjectMapper mapper = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
            .build();

    public Map<String, Object> parse(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new HashMap<>();
        }

        String trimmedText = text.trim();

        if (trimmedText.startsWith("{")) {
            try {
                JsonNode root = mapper.readTree(trimmedText);

                if (root.has("sui_image_params")) {
                    return new SwarmUIStrategy().parse(trimmedText, mapper);
                }

                Map<String, Object> results = new HashMap<>();
                ComfyUIStrategy strategy = new ComfyUIStrategy();

                if (root.has("nodes")) {
                    for (JsonNode node : root.get("nodes")) {
                        processComfyNode(node, strategy, results);
                    }
                    strategy.extract("nodes_wrapper", root, null, results);
                } else {
                    boolean isApi = false;
                    Iterator<JsonNode> it = root.elements();
                    while (it.hasNext()) {
                        if (it.next().has("class_type")) {
                            isApi = true;
                            break;
                        }
                    }

                    if (isApi) {
                        strategy.extract("api_nodes", root, null, results);
                    } else {
                        Iterator<Map.Entry<String, JsonNode>> fields = root.fields();
                        while (fields.hasNext()) {
                            Map.Entry<String, JsonNode> entry = fields.next();
                            JsonNode node = entry.getValue();
                            if (node.has("inputs") && node.has("class_type")) {
                                processComfyNode(node, strategy, results);
                            }
                        }
                    }
                }

                if (!results.isEmpty()) return results;

            } catch (Exception e) {
                logger.warn("JSON block detected but parsing failed: {}", e.getMessage());
                throw new RuntimeException("Failed to parse image generation metadata from JSON structure.", e);
            }
        }

        if (text.contains("Steps: ") && text.contains("Sampler: ")) {
            return new CommonStrategy().parse(text, mapper);
        }

        if (text.contains("\"app_version\":") && text.contains("invokeai")) {
            return new InvokeAIStrategy().parse(text, mapper);
        }

        if (text.contains("NovelAI")) {
            return new NovelAIStrategy().parse(text, mapper);
        }

        if (text.contains("sui_image_params")) {
            return new SwarmUIStrategy().parse(text, mapper);
        }

        return new HashMap<>();
    }

    private void processComfyNode(
            JsonNode node,
            ComfyUIStrategy strategy,
            Map<String, Object> results
    ) {
        Iterator<Map.Entry<String, JsonNode>> nodeFields = node.fields();
        while (nodeFields.hasNext()) {
            Map.Entry<String, JsonNode> field = nodeFields.next();
            strategy.extract(field.getKey(), field.getValue(), node, results);
        }
    }
}

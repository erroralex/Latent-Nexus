package com.nilsson.latentnexus.service.metadata;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Service for extracting metadata from uploaded image files.
 * <p>
 * This service extracts physical attributes (dimensions, size, MIME type) and
 * AI generation metadata from various image formats. It uses {@link ImageIO}
 * for dimensions and {@link ImageMetadataReader} to find metadata chunks
 * embedded in the file, which are then parsed by {@link TextParamsParser}.
 * </p>
 */
@Service
public class MetadataExtractionService {

    private static final Logger logger = LoggerFactory.getLogger(MetadataExtractionService.class);
    private final TextParamsParser textParamsParser;

    public MetadataExtractionService(TextParamsParser textParamsParser) {
        this.textParamsParser = textParamsParser;
    }

    /**
     * Extracts physical dimensions and AI generation metadata from an uploaded file.
     *
     * @param file the multipart file to process
     * @return a map containing extracted metadata and physical attributes
     */
    public Map<String, Object> extractDataFromUpload(MultipartFile file) {
        Map<String, Object> results = new HashMap<>();

        results.put("fileSizeBytes", file.getSize());
        results.put("mimeType", file.getContentType());

        extractPhysicalDimensions(file, results);

        String rawData = findBestMetadataChunk(file);
        results.putAll(textParamsParser.parse(rawData));

        return results;
    }

    private void extractPhysicalDimensions(MultipartFile file, Map<String, Object> results) {
        int width = 0;
        int height = 0;

        try (InputStream is = file.getInputStream(); ImageInputStream iis = ImageIO.createImageInputStream(is)) {
            if (iis != null) {
                Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    try {
                        reader.setInput(iis);
                        width = reader.getWidth(0);
                        height = reader.getHeight(0);
                    } finally {
                        reader.dispose();
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Failed to read dimensions using ImageIO for {}: {}", file.getOriginalFilename(), e.getMessage());
        }

        if (width > 0 && height > 0) {
            results.put("width", width);
            results.put("height", height);
        }
    }

    private String findBestMetadataChunk(MultipartFile file) {
        List<String> candidates = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(is);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    String desc = tag.getDescription();
                    if (desc == null) continue;

                    String tagName = tag.getTagName().toLowerCase();

                    if (tagName.contains("parameters") || tagName.contains("user comment") || desc.contains("Steps:")) {
                        candidates.add(desc);
                    } else if (desc.contains("{")) {
                        int braceIndex = desc.indexOf("{");
                        if (braceIndex != -1) {
                            candidates.add(desc.substring(braceIndex).trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Failed to extract metadata chunks: {}", e.getMessage());
        }

        return candidates.stream()
                .max(Comparator.comparingInt(this::scoreChunk))
                .orElse("");
    }

    private int scoreChunk(String chunk) {
        if (chunk == null) return 0;
        if (chunk.contains("sui_image_params")) return 100;
        if (chunk.matches("(?s).*\\{\\s*\"\\d+\"\\s*:\\s*\\{.*")) return 90;
        if (chunk.contains("Steps:") && chunk.contains("Sampler:")) return 80;
        if (chunk.contains("\"nodes\"") && chunk.contains("\"links\"")) return 10;
        return 0;
    }
}

package com.nilsson.latentnexus.mapper;

import com.nilsson.latentnexus.domain.dto.AssetDTO;
import com.nilsson.latentnexus.domain.entity.AssetEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between Asset-related domain entities and Data Transfer Objects (DTOs).
 * <p>
 * This class facilitates the decoupling of the internal persistence model ({@link AssetEntity}) from the 
 * public-facing API contract ({@link AssetDTO}). By centralizing the mapping logic, it ensures consistency 
 * across the application and simplifies the maintenance of data transformation rules.
 * </p>
 * <p>
 * The mapper handles the extraction of core file properties, image dimensions, and complex JSONB metadata, 
 * while also resolving relational data such as the workspace identifier.
 * </p>
 */
@Component
public class AssetMapper {

    public AssetDTO toDTO(AssetEntity entity) {
        if (entity == null) {
            return null;
        }
        return new AssetDTO(
                entity.getId(),
                entity.getFilename(),
                entity.getMimeType(),
                entity.getFileSizeBytes(),
                entity.getWidth(),
                entity.getHeight(),
                entity.getGenerationMetadata(),
                entity.getWorkspace().getId()
        );
    }
}

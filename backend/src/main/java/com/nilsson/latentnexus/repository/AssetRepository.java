package com.nilsson.latentnexus.repository;

import com.nilsson.latentnexus.domain.entity.AssetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link AssetEntity} operations.
 * <p>
 * This repository provides methods for managing AI-generated assets, including
 * finding assets by workspace and file hash, and retrieving assets for a specific
 * workspace with pagination. It also includes a native PostgreSQL query for
 * searching within the JSONB 'generation_metadata' column.
 * </p>
 */
@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, UUID> {

    /**
     * Finds an asset by its workspace ID and file hash.
     *
     * @param workspaceId
     *         the ID of the workspace
     * @param fileHash
     *         the SHA-256 hash of the file
     *
     * @return an {@link Optional} containing the found asset, or empty if not found
     */
    Optional<AssetEntity> findByWorkspaceIdAndFileHash(UUID workspaceId, String fileHash);

    /**
     * Retrieves all assets for a given workspace with pagination.
     *
     * @param workspaceId
     *         the ID of the workspace
     * @param pageable
     *         the pagination information
     *
     * @return a {@link Page} of assets
     */
    Page<AssetEntity> findByWorkspaceId(UUID workspaceId, Pageable pageable);

    /**
     * Searches for assets within a workspace based on a key-value pair in the generation metadata.
     *
     * @param workspaceId
     *         the ID of the workspace
     * @param jsonKey
     *         the key within the JSONB metadata
     * @param jsonValue
     *         the value to match
     * @param pageable
     *         the pagination information
     *
     * @return a {@link Page} of matching assets
     */
    @Query(value = """
            SELECT * FROM assets a 
            WHERE a.workspace_id = :workspaceId 
            AND a.generation_metadata ->> :jsonKey = :jsonValue
            """,
            nativeQuery = true)
    Page<AssetEntity> findByMetadataKeyValue(
            @Param("workspaceId") UUID workspaceId,
            @Param("jsonKey") String jsonKey,
            @Param("jsonValue") String jsonValue,
            Pageable pageable
    );
}

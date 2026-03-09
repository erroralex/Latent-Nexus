package com.nilsson.latentnexus.repository;

import com.nilsson.latentnexus.domain.entity.AssetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link AssetEntity} persistence operations.
 * <p>
 * This repository provides specialized data access methods for AI-generated assets, 
 * including workspace-scoped queries, deduplication checks via file hashes, and 
 * advanced JSONB metadata filtering.
 * </p>
 * <p>
 * It leverages Spring Data JPA for standard CRUD operations and incorporates 
 * custom JPQL and native PostgreSQL queries to handle complex requirements like 
 * cursor-based pagination and deep JSON metadata searches.
 * </p>
 */
@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, UUID> {

    Optional<AssetEntity> findByWorkspaceIdAndFileHash(UUID workspaceId, String fileHash);

    Page<AssetEntity> findByWorkspaceId(UUID workspaceId, Pageable pageable);

    @Query("SELECT a FROM AssetEntity a WHERE a.workspace.id = :workspaceId AND a.createdAt < :cursor ORDER BY a.createdAt DESC")
    Page<AssetEntity> findByWorkspaceIdAndCreatedAtBefore(@Param("workspaceId") UUID workspaceId, @Param("cursor") Instant cursor, Pageable pageable);

    Optional<AssetEntity> findByIdAndWorkspaceId(UUID assetId, UUID workspaceId);

    @Query(value = """
            SELECT * FROM assets a 
            WHERE a.workspace_id = :workspaceId 
            AND a.generation_metadata ->> :jsonKey ILIKE %:jsonValue%
            """,
            nativeQuery = true)
    Page<AssetEntity> findByMetadataKeyValue(
            @Param("workspaceId") UUID workspaceId,
            @Param("jsonKey") String jsonKey,
            @Param("jsonValue") String jsonValue,
            Pageable pageable
    );
}

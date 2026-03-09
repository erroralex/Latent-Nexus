package com.nilsson.latentnexus.repository;

import com.nilsson.latentnexus.domain.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link WorkspaceEntity} persistence operations.
 * <p>
 * This repository manages the lifecycle of logical workspaces within the 
 * Latent Nexus application. Workspaces serve as the primary organizational 
 * unit for grouping AI-generated assets and prompts, providing a dedicated 
 * context for data storage and access.
 * </p>
 * <p>
 * Key functionalities include finding workspaces by their unique name and 
 * checking for the existence of a workspace with a specific name.
 * </p>
 */
@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, UUID> {

    Optional<WorkspaceEntity> findByName(String name);

    boolean existsByName(String name);
}

package com.nilsson.latentnexus.repository;

import com.nilsson.latentnexus.domain.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link WorkspaceEntity} operations.
 * <p>
 * This repository provides methods for managing workspaces, including
 * finding a workspace by its unique name and checking for its existence.
 * </p>
 */
@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, UUID> {

    /**
     * Finds a workspace by its unique name.
     *
     * @param name
     *         the name of the workspace
     *
     * @return an {@link Optional} containing the found workspace, or empty if not found
     */
    Optional<WorkspaceEntity> findByName(String name);

    /**
     * Checks if a workspace exists with the given name.
     *
     * @param name
     *         the name of the workspace
     *
     * @return true if a workspace exists with the given name, false otherwise
     */
    boolean existsByName(String name);
}

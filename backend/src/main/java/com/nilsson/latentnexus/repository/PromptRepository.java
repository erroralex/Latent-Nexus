package com.nilsson.latentnexus.repository;

import com.nilsson.latentnexus.domain.entity.PromptEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for {@link PromptEntity} operations.
 * <p>
 * This repository provides methods for managing prompts, including retrieving
 * all prompts for a specific workspace with pagination and searching for
 * prompts by title within a workspace.
 * </p>
 */
@Repository
public interface PromptRepository extends JpaRepository<PromptEntity, UUID> {

    /**
     * Retrieves all prompts belonging to a specific workspace with pagination.
     *
     * @param workspaceId
     *         the ID of the workspace
     * @param pageable
     *         the pagination information
     *
     * @return a {@link Page} of prompts
     */
    Page<PromptEntity> findByWorkspaceId(UUID workspaceId, Pageable pageable);

    /**
     * Searches for prompts within a workspace where the title contains the search term.
     *
     * @param workspaceId
     *         the ID of the workspace
     * @param title
     *         the search term for the title
     * @param pageable
     *         the pagination information
     *
     * @return a {@link Page} of matching prompts
     */
    Page<PromptEntity> findByWorkspaceIdAndTitleContainingIgnoreCase(UUID workspaceId, String title, Pageable pageable);
}

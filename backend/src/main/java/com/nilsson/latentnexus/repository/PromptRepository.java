package com.nilsson.latentnexus.repository;

import com.nilsson.latentnexus.domain.entity.PromptEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for {@link PromptEntity} persistence operations.
 * <p>
 * This repository manages the lifecycle of AI generation prompts, providing 
 * workspace-scoped data access and search capabilities. It ensures that 
 * prompts are correctly isolated by workspace and can be retrieved efficiently 
 * using standard Spring Data JPA query methods.
 * </p>
 * <p>
 * Key functionalities include paginated retrieval of prompts for a specific 
 * workspace and case-insensitive title-based searching.
 * </p>
 */
@Repository
public interface PromptRepository extends JpaRepository<PromptEntity, UUID> {

    Page<PromptEntity> findByWorkspaceId(UUID workspaceId, Pageable pageable);

    Page<PromptEntity> findByWorkspaceIdAndTitleContainingIgnoreCase(UUID workspaceId, String title, Pageable pageable);
}

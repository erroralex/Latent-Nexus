package com.nilsson.latentnexus.service;

import com.nilsson.latentnexus.domain.entity.WorkspaceEntity;
import com.nilsson.latentnexus.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing logical workspaces.
 * <p>
 * This service provides the core business logic for the lifecycle of workspaces within the Latent Nexus application.
 * Workspaces serve as the primary organizational unit for grouping AI-generated assets and prompts, providing
 * a dedicated context for data storage and access.
 * </p>
 * <p>
 * Key responsibilities include:
 * <ul>
 *     <li>Creating new workspaces while ensuring name uniqueness.</li>
 *     <li>Retrieving individual workspaces by their unique identifier.</li>
 *     <li>Listing all available workspaces in the system.</li>
 *     <li>Managing the deletion of workspaces and their associated data.</li>
 * </ul>
 * All state-changing operations are executed within a transactional context to ensure data consistency.
 * </p>
 */
@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public List<WorkspaceEntity> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }

    public Optional<WorkspaceEntity> getWorkspaceById(UUID id) {
        return workspaceRepository.findById(id);
    }

    @Transactional
    public WorkspaceEntity createWorkspace(String name, String description) {
        if (workspaceRepository.existsByName(name)) {
            throw new RuntimeException("Workspace with name '" + name + "' already exists.");
        }

        WorkspaceEntity workspace = new WorkspaceEntity(name, description);
        return workspaceRepository.save(workspace);
    }

    @Transactional
    public void deleteWorkspace(UUID id) {
        workspaceRepository.deleteById(id);
    }
}

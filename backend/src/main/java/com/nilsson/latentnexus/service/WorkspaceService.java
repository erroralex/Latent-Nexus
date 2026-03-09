package com.nilsson.latentnexus.service;

import com.nilsson.latentnexus.domain.entity.WorkspaceEntity;
import com.nilsson.latentnexus.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing logical workspaces in the Latent Nexus application.
 * <p>
 * This service provides the core business logic for creating, retrieving, and
 * deleting workspaces. It ensures that workspace names are unique and manages
 * the persistence of workspace entities through the {@link WorkspaceRepository}.
 * All operations that modify the database are performed within a transactional context.
 * </p>
 */
@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * Retrieves all workspaces available in the system.
     *
     * @return a list of all {@link WorkspaceEntity} objects
     */
    public List<WorkspaceEntity> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }

    /**
     * Finds a specific workspace by its unique ID.
     *
     * @param id
     *         the UUID of the workspace to find
     *
     * @return an {@link Optional} containing the found workspace, or empty if not found
     */
    public Optional<WorkspaceEntity> getWorkspaceById(UUID id) {
        return workspaceRepository.findById(id);
    }

    /**
     * Creates a new workspace with the given name and description.
     *
     * @param name
     *         the unique name of the workspace
     * @param description
     *         an optional description of the workspace
     *
     * @return the saved {@link WorkspaceEntity}
     *
     * @throws RuntimeException
     *         if a workspace with the same name already exists
     */
    @Transactional
    public WorkspaceEntity createWorkspace(String name, String description) {
        if (workspaceRepository.existsByName(name)) {
            throw new RuntimeException("Workspace with name '" + name + "' already exists.");
        }

        WorkspaceEntity workspace = new WorkspaceEntity(name, description);
        return workspaceRepository.save(workspace);
    }

    /**
     * Deletes a workspace by its unique ID.
     *
     * @param id
     *         the UUID of the workspace to delete
     */
    @Transactional
    public void deleteWorkspace(UUID id) {
        workspaceRepository.deleteById(id);
    }
}

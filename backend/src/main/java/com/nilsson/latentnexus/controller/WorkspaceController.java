package com.nilsson.latentnexus.controller;

import com.nilsson.latentnexus.domain.entity.WorkspaceEntity;
import com.nilsson.latentnexus.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing logical workspaces within the Latent Nexus application.
 * <p>
 * This controller provides endpoints for creating, retrieving, and listing workspaces.
 * Workspaces serve as the primary organizational unit for grouping AI-generated assets
 * and prompts. Access to these endpoints is controlled via Spring Security, with
 * specific roles required for different operations (e.g., 'ADMIN' for creation).
 * </p>
 */
@RestController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    /**
     * Retrieves a list of all available workspaces.
     *
     * @return a list of {@link WorkspaceEntity} objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<WorkspaceEntity> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    /**
     * Creates a new workspace.
     *
     * @param workspace
     *         the workspace entity containing the name and description
     *
     * @return a {@link ResponseEntity} containing the created {@link WorkspaceEntity}
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkspaceEntity> createWorkspace(@RequestBody WorkspaceEntity workspace) {
        WorkspaceEntity created = workspaceService.createWorkspace(workspace.getName(), workspace.getDescription());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Retrieves a specific workspace by its unique identifier.
     *
     * @param id
     *         the UUID of the workspace to retrieve
     *
     * @return a {@link ResponseEntity} containing the {@link WorkspaceEntity}, or a 404 Not Found response
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<WorkspaceEntity> getWorkspace(@PathVariable UUID id) {
        return workspaceService.getWorkspaceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

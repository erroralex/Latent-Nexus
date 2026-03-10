package com.nilsson.latentnexus.controller;

import com.nilsson.latentnexus.domain.entity.WorkspaceEntity;
import com.nilsson.latentnexus.service.WorkspaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing logical workspaces within the Latent Nexus application.
 * <p>
 * This class provides a set of RESTful endpoints for performing CRUD (Create, Read, Update, Delete)
 * operations on {@link WorkspaceEntity} objects. Workspaces serve as the fundamental
 * organizational unit within the application, allowing users to group and manage
 * their AI-generated assets and prompts in isolated environments.
 * </p>
 * <p>
 * Access to these endpoints is secured using Spring Security's `@PreAuthorize` annotations,
 * enforcing role-based access control. For instance, creating a new workspace might
 * be restricted to 'ADMIN' users, while viewing workspaces could be available to both 'USER' and 'ADMIN' roles.
 * </p>
 * <p>
 * The controller interacts with the `WorkspaceService` to perform business logic
 * and data persistence operations, ensuring a clean separation of concerns.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceController.class);
    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<WorkspaceEntity> getAllWorkspaces() {
        List<WorkspaceEntity> workspaces = workspaceService.getAllWorkspaces();
        log.info("Retrieved {} workspaces for authenticated user", workspaces.size());
        return workspaces;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkspaceEntity> createWorkspace(@RequestBody WorkspaceEntity workspace) {
        WorkspaceEntity created = workspaceService.createWorkspace(workspace.getName(), workspace.getDescription());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<WorkspaceEntity> getWorkspace(@PathVariable UUID id) {
        return workspaceService.getWorkspaceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

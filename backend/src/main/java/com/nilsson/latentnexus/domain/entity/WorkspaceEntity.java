package com.nilsson.latentnexus.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a logical workspace within the Latent Nexus application.
 * <p>
 * Workspaces serve as the primary organizational unit for users to manage and
 * isolate their AI-generated assets and prompts. Each workspace is uniquely
 * identified and provides a dedicated context for data storage and access.
 * </p>
 * <p>
 * Key attributes of a workspace include:
 * <ul>
 *     <li>**Name:** A unique, human-readable identifier for the workspace. This name
 *         must be unique across all workspaces in the system.</li>
 *     <li>**Description:** An optional, longer text providing more details about
 *         the purpose or contents of the workspace.</li>
 * </ul>
 * As a subclass of {@link BaseEntity}, `WorkspaceEntity` automatically inherits
 * `id`, `createdAt`, and `updatedAt` fields, providing essential auditing information.
 * </p>
 */
@Entity
@Table(name = "workspaces")
public class WorkspaceEntity extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    public WorkspaceEntity() {
    }

    public WorkspaceEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

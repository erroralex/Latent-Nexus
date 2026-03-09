package com.nilsson.latentnexus.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a logical workspace within the Latent Nexus application.
 * <p>
 * Workspaces are used to group related assets and prompts. Each workspace
 * has a unique name and an optional description.
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

package com.nilsson.latentnexus.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a prompt used for AI generation within a workspace.
 * <p>
 * This entity stores the positive and negative prompts, a title for identification,
 * and the target model for which the prompt was designed. Each prompt is
 * associated with a specific {@link WorkspaceEntity}.
 * </p>
 */
@Entity
@Table(name = "prompts")
public class PromptEntity extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "positive_prompt", columnDefinition = "TEXT", nullable = false)
    private String positivePrompt;

    @Column(name = "negative_prompt", columnDefinition = "TEXT")
    private String negativePrompt;

    @Column(name = "target_model")
    private String targetModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPositivePrompt() {
        return positivePrompt;
    }

    public void setPositivePrompt(String positivePrompt) {
        this.positivePrompt = positivePrompt;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }

    public String getTargetModel() {
        return targetModel;
    }

    public void setTargetModel(String targetModel) {
        this.targetModel = targetModel;
    }

    public WorkspaceEntity getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceEntity workspace) {
        this.workspace = workspace;
    }
}

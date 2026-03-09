package com.nilsson.latentnexus.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a prompt used for AI generation within a specific workspace in the Latent Nexus system.
 * <p>
 * This entity captures the details of a prompt that can be utilized by various AI models
 * to generate content. Each prompt is uniquely identified and is always associated
 * with a {@link WorkspaceEntity}, ensuring multi-tenancy and proper organization.
 * </p>
 * <p>
 * Key attributes include:
 * <ul>
 *     <li>**Title:** A human-readable title for easy identification and management of the prompt.</li>
 *     <li>**Positive Prompt:** The core textual instruction or description provided to the AI model
 *         to guide its generation towards desired characteristics.</li>
 *     <li>**Negative Prompt:** Optional textual instructions to guide the AI model away from
 *         undesired characteristics or elements in the generated output.</li>
 *     <li>**Target Model:** An optional identifier for the specific AI model or type of model
 *         for which this prompt is optimized or intended.</li>
 * </ul>
 * The `workspace` field establishes a many-to-one relationship with `WorkspaceEntity`,
 * linking the prompt to its organizational context.
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

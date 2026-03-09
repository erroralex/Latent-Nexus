package com.nilsson.latentnexus.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;
import java.util.UUID;

/**
 * Abstract base class for all JPA entities within the Latent Nexus application.
 * <p>
 * This class provides a common foundation for persistent entities, encapsulating
 * audit-related fields that are typically present in most database tables.
 * It automatically manages the generation of a unique identifier (UUID) and
 * tracks the creation and last update timestamps for each entity.
 * </p>
 * <p>
 * Key features include:
 * <ul>
 *     <li>**UUID Primary Key:** A universally unique identifier (`id`) is generated
 *         automatically upon entity persistence, ensuring uniqueness across distributed systems.</li>
 *     <li>**Creation Timestamp (`createdAt`):** Records the exact moment an entity
 *         was first persisted. This field is set automatically during the `PrePersist`
 *         lifecycle event and is immutable thereafter.</li>
 *     <li>**Update Timestamp (`updatedAt`):** Records the last moment an entity
 *         was modified. This field is updated automatically during both `PrePersist`
 *         and `PreUpdate` lifecycle events.</li>
 * </ul>
 * The timestamps are managed using JPA lifecycle callbacks (`@PrePersist` and `@PreUpdate`),
 * ensuring consistency and reducing boilerplate code in subclasses.
 * </p>
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

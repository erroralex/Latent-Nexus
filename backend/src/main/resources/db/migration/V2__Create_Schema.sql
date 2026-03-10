-- V2: Initial Consolidated Enterprise Schema for Latent Nexus
-- Engineered for PostgreSQL 16+ with pgvector support

-------------------------------------------------------------------------------
-- 1. EXTENSIONS
-------------------------------------------------------------------------------
-- Enable pgvector extension for semantic search
CREATE EXTENSION IF NOT EXISTS vector;

-------------------------------------------------------------------------------
-- 2. WORKSPACES
-------------------------------------------------------------------------------
CREATE TABLE workspaces
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    name        VARCHAR(255)             NOT NULL UNIQUE,
    description VARCHAR(500)
);

-------------------------------------------------------------------------------
-- 3. PROMPTS
-------------------------------------------------------------------------------
CREATE TABLE prompts
(
    id              UUID PRIMARY KEY,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    title           VARCHAR(255)             NOT NULL,
    positive_prompt TEXT                     NOT NULL,
    negative_prompt TEXT,
    target_model    VARCHAR(255),
    workspace_id    UUID                     NOT NULL,

    -- Foreign Key Constraint: If a workspace is deleted, delete its prompts
    CONSTRAINT fk_prompts_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces (id)
            ON DELETE CASCADE
);

-- Index to speed up workspace-specific prompt queries
CREATE INDEX idx_prompts_workspace_id ON prompts (workspace_id);

-------------------------------------------------------------------------------
-- 4. ASSETS (Images & Workflows)
-------------------------------------------------------------------------------
CREATE TABLE assets
(
    id                  UUID PRIMARY KEY,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    filename            VARCHAR(255)             NOT NULL,
    file_hash           VARCHAR(64)              NOT NULL UNIQUE,
    storage_path        VARCHAR(255)             NOT NULL,
    workspace_id        UUID                     NOT NULL,

    -- Standard File Properties
    mime_type           VARCHAR(100)             NOT NULL,
    file_size_bytes     BIGINT                   NOT NULL,
    width               INTEGER,
    height              INTEGER,

    -- The JSONB column for ComfyUI/Stable Diffusion metadata
    generation_metadata JSONB,

    -- Vector embedding for semantic search (768 dimensions for Gemini)
    embedding           vector(768),

    CONSTRAINT fk_assets_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces (id)
            ON DELETE CASCADE
);

-- Index to speed up workspace-specific asset gallery loading
CREATE INDEX idx_assets_workspace_id ON assets (workspace_id);

-- GIN INDEX (Generalized Inverted Index) for JSONB
-- This makes searching deeply nested JSON graphs instant
CREATE INDEX idx_assets_metadata_gin ON assets USING GIN (generation_metadata);

-- Targeted GIN index for tag-based searches
CREATE INDEX idx_assets_tags_gin
    ON assets USING GIN ((generation_metadata -> 'tags'));

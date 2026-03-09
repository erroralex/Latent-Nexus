package com.nilsson.latentnexus.controller;

import com.nilsson.latentnexus.domain.entity.AssetEntity;
import com.nilsson.latentnexus.service.AssetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * REST controller for managing AI-generated assets.
 * <p>
 * This controller provides endpoints for uploading assets to a specific workspace.
 * It handles multipart file uploads and delegates the processing and metadata
 * extraction to the {@link AssetService}. Access is restricted to users with
 * 'USER' or 'ADMIN' roles.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/workspaces/{workspaceId}/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    /**
     * Uploads an AI-generated image and automatically extracts generation metadata.
     *
     * @param workspaceId
     *         the ID of the workspace to which the asset belongs
     * @param file
     *         the multipart file to upload
     *
     * @return a {@link ResponseEntity} containing the saved {@link AssetEntity}
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AssetEntity> uploadAsset(
            @PathVariable UUID workspaceId,
            @RequestParam("file") MultipartFile file) {
        try {
            AssetEntity savedAsset = assetService.uploadAsset(workspaceId, file);
            return new ResponseEntity<>(savedAsset, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.nilsson.latentnexus.controller;

import com.nilsson.latentnexus.domain.dto.AssetDTO;
import com.nilsson.latentnexus.domain.dto.PageDTO;
import com.nilsson.latentnexus.domain.entity.AssetEntity;
import com.nilsson.latentnexus.exception.AssetNotFoundException;
import com.nilsson.latentnexus.mapper.AssetMapper;
import com.nilsson.latentnexus.service.AssetService;
import com.nilsson.latentnexus.service.ObjectStorageService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for managing AI-generated assets within specific workspaces.
 * <p>
 * This controller provides a comprehensive API for asset lifecycle management, including
 * uploading new assets, retrieving paginated lists of assets, and serving asset images
 * via pre-signed URLs. All operations are secured using Spring Security with Keycloak,
 * ensuring that only authenticated users with appropriate roles (`USER` or `ADMIN`)
 * can access and manipulate assets within their assigned workspaces.
 * </p>
 * <p>
 * Key functionalities include:
 * <ul>
 *     <li>**Asset Upload:** Allows users to upload new AI-generated image files. The system
 *         automatically extracts metadata and enriches it using the Gemini API, storing
 *         both the binary file in S3-compatible storage and metadata in PostgreSQL.</li>
 *     <li>**Paginated Asset Retrieval:** Provides an efficient, cursor-based pagination
 *         mechanism to fetch assets for a given workspace, optimizing performance for
 *         large galleries.</li>
 *     <li>**Secure Image Serving:** Generates temporary, pre-signed URLs for direct access
 *         to asset images stored in object storage, ensuring secure and controlled access
 *         without exposing backend credentials.</li>
 * </ul>
 * The controller leverages DTOs (`AssetDTO`, `PageDTO`) to decouple the API contract
 * from the internal persistence model, and uses an `AssetMapper` for conversion.
 * Robust exception handling is managed globally via `@ControllerAdvice`.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/workspaces/{workspaceId}/assets")
public class AssetController {

    private final AssetService assetService;
    private final ObjectStorageService objectStorageService;
    private final AssetMapper assetMapper;

    public AssetController(AssetService assetService, ObjectStorageService objectStorageService, AssetMapper assetMapper) {
        this.assetService = assetService;
        this.objectStorageService = objectStorageService;
        this.assetMapper = assetMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PageDTO<AssetDTO>> getAssets(
            @PathVariable UUID workspaceId,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) UUID cursor) {

        Page<AssetEntity> assetPage = assetService.getAssetsByWorkspacePaginated(workspaceId, cursor, limit);
        List<AssetDTO> assetDTOs = assetPage.getContent().stream()
                .map(assetMapper::toDTO)
                .collect(Collectors.toList());

        String nextCursor = assetPage.hasNext() ? assetPage.getContent().get(assetPage.getContent().size() - 1).getId().toString() : null;

        return ResponseEntity.ok(new PageDTO<>(assetDTOs, nextCursor));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AssetDTO> uploadAsset(
            @PathVariable UUID workspaceId,
            @RequestParam("file") MultipartFile file) {
        AssetEntity savedAsset = assetService.uploadAsset(workspaceId, file);
        AssetDTO savedAssetDTO = assetMapper.toDTO(savedAsset);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAsset.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedAssetDTO);
    }

    @GetMapping("/{assetId}/image")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> getAssetImage(
            @PathVariable UUID workspaceId,
            @PathVariable UUID assetId) {
        AssetEntity asset = assetService.getAssetByIdAndWorkspace(assetId, workspaceId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with id: " + assetId));

        String presignedUrl = objectStorageService.getPresignedUrl(asset.getStoragePath());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(presignedUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}

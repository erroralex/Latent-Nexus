package com.nilsson.latentnexus.service;

import com.nilsson.latentnexus.domain.entity.AssetEntity;
import com.nilsson.latentnexus.domain.entity.WorkspaceEntity;
import com.nilsson.latentnexus.exception.AssetNotFoundException;
import com.nilsson.latentnexus.repository.AssetRepository;
import com.nilsson.latentnexus.repository.WorkspaceRepository;
import com.nilsson.latentnexus.service.metadata.MetadataExtractionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing AI-generated assets.
 * <p>
 * This service handles the core business logic for assets, including uploading new files,
 * extracting and enriching metadata, and retrieving assets with workspace-scoped pagination.
 * It orchestrates interactions between the repository layer, object storage, and AI enrichment services.
 * </p>
 * <p>
 * Key responsibilities include:
 * <ul>
 *     <li>Processing multipart file uploads and ensuring data integrity via SHA-256 hashing.</li>
 *     <li>Triggering metadata extraction from image chunks (e.g., ComfyUI, Stable Diffusion).</li>
 *     <li>Invoking the Gemini API for semantic enrichment and vector embedding generation.</li>
 *     <li>Managing secure storage of binary data in S3-compatible systems.</li>
 *     <li>Providing cursor-based paginated access to assets within a specific workspace.</li>
 * </ul>
 * </p>
 */
@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MetadataExtractionService metadataService;
    private final GeminiOrchestrator geminiOrchestrator;
    private final ObjectStorageService objectStorageService;

    public AssetService(AssetRepository assetRepository,
                        WorkspaceRepository workspaceRepository,
                        MetadataExtractionService metadataService,
                        GeminiOrchestrator geminiOrchestrator,
                        ObjectStorageService objectStorageService) {
        this.assetRepository = assetRepository;
        this.workspaceRepository = workspaceRepository;
        this.metadataService = metadataService;
        this.geminiOrchestrator = geminiOrchestrator;
        this.objectStorageService = objectStorageService;
    }

    @Transactional
    public AssetEntity uploadAsset(UUID workspaceId, MultipartFile file) {
        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        try {
            String fileHash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(file.getInputStream());

            return assetRepository.findByWorkspaceIdAndFileHash(workspaceId, fileHash)
                    .orElseGet(() -> {
                        try {
                            Map<String, Object> allData = metadataService.extractDataFromUpload(file);

                            AssetEntity asset = new AssetEntity();
                            asset.setFilename(file.getOriginalFilename());
                            asset.setFileHash(fileHash);
                            asset.setWorkspace(workspace);

                            asset.setMimeType((String) allData.remove("mimeType"));
                            asset.setFileSizeBytes((Long) allData.remove("fileSizeBytes"));
                            asset.setWidth((Integer) allData.remove("width"));
                            asset.setHeight((Integer) allData.remove("height"));

                            asset.setGenerationMetadata(allData);

                            geminiOrchestrator.enrichAssetMetadata(asset);

                            String storagePath = "vault/" + workspaceId + "/" + file.getOriginalFilename();
                            asset.setStoragePath(storagePath);

                            objectStorageService.uploadFile(file, storagePath);

                            return assetRepository.save(asset);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to process image metadata", e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload asset", e);
        }
    }

    public Page<AssetEntity> getAssetsByWorkspacePaginated(UUID workspaceId, UUID cursor, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        if (cursor == null) {
            return assetRepository.findByWorkspaceId(workspaceId, pageRequest);
        } else {
            Instant cursorTimestamp = assetRepository.findById(cursor)
                    .orElseThrow(() -> new AssetNotFoundException("Cursor asset not found with id: " + cursor))
                    .getCreatedAt();
            return assetRepository.findByWorkspaceIdAndCreatedAtBefore(workspaceId, cursorTimestamp, pageRequest);
        }
    }

    public Optional<AssetEntity> getAssetByIdAndWorkspace(UUID assetId, UUID workspaceId) {
        return assetRepository.findByIdAndWorkspaceId(assetId, workspaceId);
    }
}

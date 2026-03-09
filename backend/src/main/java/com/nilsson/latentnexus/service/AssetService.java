package com.nilsson.latentnexus.service;

import com.nilsson.latentnexus.domain.entity.AssetEntity;
import com.nilsson.latentnexus.domain.entity.WorkspaceEntity;
import com.nilsson.latentnexus.repository.AssetRepository;
import com.nilsson.latentnexus.repository.WorkspaceRepository;
import com.nilsson.latentnexus.service.metadata.MetadataExtractionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

/**
 * Service for managing AI-generated assets.
 * <p>
 * This service handles the uploading and processing of assets, including
 * workspace validation, duplicate detection using SHA-256 hashing,
 * metadata extraction via {@link MetadataExtractionService}, and
 * persistence to the database.
 * </p>
 */
@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MetadataExtractionService metadataService;

    public AssetService(AssetRepository assetRepository,
                        WorkspaceRepository workspaceRepository,
                        MetadataExtractionService metadataService) {
        this.assetRepository = assetRepository;
        this.workspaceRepository = workspaceRepository;
        this.metadataService = metadataService;
    }

    /**
     * Uploads and processes a new asset for a given workspace.
     *
     * @param workspaceId
     *         the ID of the workspace to which the asset belongs
     * @param file
     *         the multipart file to upload
     *
     * @return the saved {@link AssetEntity}
     *
     * @throws Exception
     *         if an error occurs during processing or metadata extraction
     */
    @Transactional
    public AssetEntity uploadAsset(UUID workspaceId, MultipartFile file) throws Exception {
        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

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

                        // TODO: Implement actual file storage (S3 or Local Disk)
                        asset.setStoragePath("vault/" + workspaceId + "/" + fileHash);

                        return assetRepository.save(asset);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to process image metadata", e);
                    }
                });
    }
}

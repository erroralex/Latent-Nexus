package com.nilsson.latentnexus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

/**
 * Service class for interacting with S3-compatible object storage.
 * <p>
 * This service provides a high-level API for managing binary files in object storage (e.g., AWS S3, MinIO).
 * It handles file uploads and the generation of temporary pre-signed URLs for secure asset retrieval.
 * </p>
 * <p>
 * Key responsibilities include:
 * <ul>
 *     <li>Uploading multipart files to a configured bucket using the AWS SDK for Java v2.</li>
 *     <li>Generating pre-signed GET URLs with a limited expiration time (e.g., 10 minutes) to grant
 *         temporary access to private objects.</li>
 * </ul>
 * The service is configured via application properties for the target bucket name and uses
 * injected S3Client and S3Presigner beans for its operations.
 * </p>
 */
@Service
public class ObjectStorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public ObjectStorageService(S3Client s3Client, S3Presigner s3Presigner, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }

    public void uploadFile(MultipartFile file, String key) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    public String getPresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toString();
    }
}

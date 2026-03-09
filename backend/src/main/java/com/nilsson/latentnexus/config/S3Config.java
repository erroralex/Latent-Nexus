package com.nilsson.latentnexus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

/**
 * Configuration class for Amazon S3 (or S3-compatible storage like MinIO) client and presigner.
 * <p>
 * This class is responsible for setting up and providing `S3Client` and `S3Presigner` beans
 * to be used throughout the application for interacting with object storage. It retrieves
 * necessary configuration details such as endpoint, region, access key ID, and secret access key
 * from the application's properties, typically defined in `application.yaml` or environment variables.
 * </p>
 * <p>
 * The `S3Client` is used for standard S3 operations (e.g., uploading, downloading, deleting objects),
 * while the `S3Presigner` is used to generate pre-signed URLs, which grant temporary access
 * to specific S3 objects without exposing long-lived credentials. This is crucial for securely
 * serving private assets directly from the client-side.
 * </p>
 */
@Configuration
public class S3Config {

    @Value("${aws.s3.endpoint}")
    private String endpoint;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key-id}")
    private String accessKeyId;

    @Value("${aws.s3.secret-access-key}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();
    }
}

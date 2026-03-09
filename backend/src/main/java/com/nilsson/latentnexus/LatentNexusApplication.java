package com.nilsson.latentnexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Latent Nexus backend application.
 * <p>
 * This class initializes the Spring Boot application context and starts the embedded server.
 * Latent Nexus is a platform designed for managing and organizing AI-generated assets,
 * providing features such as metadata extraction, workspace management, and asset indexing.
 * </p>
 */
@SpringBootApplication
public class LatentNexusApplication {

    /**
     * Main method to launch the Spring Boot application.
     *
     * @param args
     *         command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(LatentNexusApplication.class, args);
    }

}
package com.nilsson.latentnexus.service;

import com.nilsson.latentnexus.repository.WorkspaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component responsible for seeding initial data into the database upon application startup.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private final WorkspaceService workspaceService;
    private final WorkspaceRepository workspaceRepository;

    public DataSeeder(WorkspaceService workspaceService, WorkspaceRepository workspaceRepository) {
        this.workspaceService = workspaceService;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public void run(String... args) {
        long count = workspaceRepository.count();
        log.info("Current workspace count: {}", count);
        
        if (count == 0) {
            log.info("No workspaces found. Seeding default environment...");
            workspaceService.createWorkspace(
                    "Personal Vault", 
                    "Default workspace for personal AI asset management and experimentation."
            );
            log.info("Default workspace 'Personal Vault' created. New count: {}", workspaceRepository.count());
        }
    }
}

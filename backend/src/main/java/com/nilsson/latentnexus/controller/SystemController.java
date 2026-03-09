package com.nilsson.latentnexus.controller;

import org.keycloak.admin.client.Keycloak;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for system-level operations and health checks.
 * <p>
 * This class provides endpoints to monitor the operational status and connectivity of the
 * Latent Nexus backend's critical infrastructure components. It offers a publicly
 * accessible health check endpoint that verifies the reachability and basic functionality
 * of the PostgreSQL database and the Keycloak authentication server.
 * </p>
 * <p>
 * The primary purpose of this controller is to provide a quick and reliable way
 * for external monitoring systems or load balancers to ascertain the health
 * of the application instance.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    private final JdbcTemplate jdbcTemplate;
    private final Keycloak keycloak;

    public SystemController(JdbcTemplate jdbcTemplate, Keycloak keycloak) {
        this.jdbcTemplate = jdbcTemplate;
        this.keycloak = keycloak;
    }

    /**
     * Performs a health check of the backend's critical dependencies.
     * <p>
     * This method attempts to connect to the configured PostgreSQL database and
     * the Keycloak authentication server to verify their operational status.
     * </p>
     *
     * @return a map containing the health status ("UP" or "DOWN" with an error message)
     * for the database, Keycloak, and the overall backend service.
     */
    @GetMapping("/health")
    public Map<String, String> getHealth() {
        Map<String, String> status = new HashMap<>();

        try {
            jdbcTemplate.execute("SELECT 1");
            status.put("database", "UP");
        } catch (Exception e) {
            status.put("database", "DOWN: " + e.getMessage());
        }

        try {
            keycloak.serverInfo().getInfo();
            status.put("keycloak", "UP");
        } catch (Exception e) {
            status.put("keycloak", "DOWN: " + e.getMessage());
        }

        status.put("backend", "UP");
        return status;
    }
}

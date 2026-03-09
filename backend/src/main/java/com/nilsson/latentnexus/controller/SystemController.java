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
 * This controller provides endpoints to monitor the health of the backend
 * infrastructure, including connectivity to the PostgreSQL database and
 * the Keycloak authentication server. The health check endpoint is
 * publicly accessible.
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
     * Checks the health of the backend infrastructure.
     *
     * @return a map containing the health status of the database, Keycloak, and backend
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

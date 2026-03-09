package com.nilsson.latentnexus.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the Keycloak Admin Client.
 * <p>
 * This class provides a Spring {@link Bean} for the {@link Keycloak} admin client,
 * which is used to interact with the Keycloak server's administrative API.
 * It uses client credentials flow for authentication, with properties loaded
 * from the application configuration.
 * </p>
 */
@Configuration
public class KeycloakAdminClientConfig {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${keycloak.admin.client-secret}")
    private String adminClientSecret;

    /**
     * Creates and configures a Keycloak admin client bean.
     *
     * @return a configured {@link Keycloak} instance
     */
    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(adminClientId)
                .clientSecret(adminClientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}

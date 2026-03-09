package com.nilsson.latentnexus.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up and providing a Keycloak Admin Client.
 * <p>
 * This class is responsible for creating and configuring a Spring {@link Bean} of type
 * {@link Keycloak}, which serves as the administrative client for interacting with the
 * Keycloak server's REST API. This client is essential for performing administrative
 * operations programmatically, such as managing users, roles, clients, or realms within Keycloak.
 * </p>
 * <p>
 * The client is built using the `KeycloakBuilder` and is configured with details
 * like the Keycloak server URL, the target realm, and the administrative client's
 * ID and secret. These configuration values are injected from the application's
 * properties (e.g., `application.yaml` or environment variables), ensuring
 * flexibility and environment-specific deployments.
 * </p>
 * <p>
 * Authentication for the admin client is performed using the `CLIENT_CREDENTIALS`
 * grant type, which is suitable for server-to-server communication where the
 * application itself acts as a client to Keycloak's administrative interface.
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

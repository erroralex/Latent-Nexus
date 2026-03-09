package com.nilsson.latentnexus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Security configuration for the Latent Nexus backend application.
 * <p>
 * This class sets up Spring Security to integrate with Keycloak as an OpenID Connect (OIDC)
 * provider, enabling robust authentication and authorization for the REST API.
 * </p>
 * <p>
 * Key features configured here include:
 * <ul>
 *     <li>Disabling CSRF protection, as it's typically not required for stateless REST APIs.</li>
 *     <li>Enforcing stateless session management, which is crucial for scalable, token-based authentication.</li>
 *     <li>Defining authorization rules for API endpoints, allowing public access to health checks
 *         and requiring authentication for all other requests.</li>
 *     <li>Configuring OAuth2 Resource Server to process JWTs issued by Keycloak.</li>
 *     <li>A custom {@link JwtAuthenticationConverter} that extracts Keycloak realm roles
 *         and maps them to Spring Security's {@link GrantedAuthority} objects, prefixed with "ROLE_".
 *         This allows for role-based access control using Spring Security's `@PreAuthorize` annotations.</li>
 * </ul>
 * The `@EnableMethodSecurity(prePostEnabled = true)` annotation enables method-level security,
 * allowing fine-grained access control directly on service methods or controller endpoints.
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/system/health").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtConverter;
    }

    static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        @SuppressWarnings("unchecked")
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

            if (realmAccess == null || realmAccess.isEmpty()) {
                return new ArrayList<>();
            }

            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            if (roles == null || roles.isEmpty()) {
                return new ArrayList<>();
            }

            return roles.stream()
                    .map(roleName -> "ROLE_" + roleName.toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
}

package com.simplesalesman.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Spring Security configuration for the SimpleSalesman application.
 *
 * This configuration enables:
 * - OAuth2 Resource Server support using JWT tokens issued by Keycloak
 * - URL-based access rules for securing REST API endpoints
 * - JWT to Spring Security authority conversion
 * - Method-level access control via annotations
 *
 * Security considerations:
 * - Stateless security using Bearer tokens
 * - CSRF disabled for APIs
 * - Supports granular role-based access via Keycloak realm roles
 *
 * @author SimpleSalesman Team
 * @version 0.0.5
 * @since 0.0.1
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Configures HTTP security including authorization rules and JWT resource server.
     *
     * @param http the HttpSecurity builder
     * @return the configured SecurityFilterChain
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Initializing Security Filter Chain for SimpleSalesman");

        http
            // Disable CSRF for stateless REST APIs using JWT
            .csrf(csrf -> {
                csrf.disable();
                log.debug("CSRF protection disabled for REST API");
            })

            // Define access rules for API endpoints
            .authorizeHttpRequests(auth -> {
                auth
                    .requestMatchers("/api/v1/import").permitAll()  // public endpoint
                    .requestMatchers("/api/**").authenticated()     // all other API endpoints require auth
                    .anyRequest().permitAll();                      // non-API requests are allowed

                log.debug("Authorization rules set: /api/v1/import is public, /api/** requires authentication");
            })

            // Enable OAuth2 resource server with JWT validation
            .oauth2ResourceServer(oauth2 -> {
                oauth2.jwt(jwt -> {
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
                    log.debug("JWT Authentication Converter registered");
                });
                log.info("OAuth2 Resource Server with JWT enabled");
            });

        log.info("Security Filter Chain successfully configured");
        return http.build();
    }

    /**
     * Creates a converter to extract granted authorities from JWT claims.
     *
     * @return a configured JwtAuthenticationConverter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        log.debug("Creating JWT Authentication Converter");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        // Set custom converter to extract realm roles from Keycloak token
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthoritiesWithLogging);

        // Use preferred_username as principal name
        converter.setPrincipalClaimName("preferred_username");

        log.debug("JWT Authentication Converter created with custom role extraction");
        return converter;
    }

    /**
     * Extracts authorities (roles) from a JWT issued by Keycloak and logs authentication metadata.
     *
     * @param jwt the decoded JWT token
     * @return collection of GrantedAuthority based on Keycloak realm roles
     */
    private Collection<GrantedAuthority> extractAuthoritiesWithLogging(Jwt jwt) {
        // Extract user info
        String username = jwt.getClaimAsString("preferred_username");
        String subject = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");

        log.debug("Processing JWT for user: username='{}', sub='{}', email='{}', name='{}'",
                username, subject, email, name);

        // Attempt to extract realm roles
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");

        if (realmAccess != null && realmAccess.containsKey("roles")) {
            @SuppressWarnings("unchecked")
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");

            List<GrantedAuthority> authorities = roles.stream()
                .filter(role -> role != null && !role.trim().isEmpty())
                .map(role -> {
                    String authority = "ROLE_" + role.toUpperCase();
                    log.trace("Mapping Keycloak role '{}' to authority '{}'", role, authority);
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toList());

            List<String> roleNames = roles.stream()
                .filter(role -> role != null && !role.trim().isEmpty())
                .map(String::toUpperCase)
                .toList();

            log.info("User '{}' (sub={}) authenticated with {} roles: {}",
                    username, subject, roleNames.size(), roleNames);

            if (log.isDebugEnabled()) {
                log.debug("JWT Exp: {}, Issued At: {}, Issuer: {}",
                        jwt.getExpiresAt(), jwt.getIssuedAt(), jwt.getIssuer());
            }

            return authorities;
        }

        // No roles found
        log.warn("User '{}' (sub={}) has no realm_access.roles in JWT – access may be restricted",
                username, subject);

        if (log.isDebugEnabled()) {
            log.debug("Available JWT claims: {}", jwt.getClaims().keySet());
            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
            if (resourceAccess != null) {
                log.debug("Resource access claims found: {}", resourceAccess.keySet());
            }
        }

        return Collections.emptyList();
    }
}

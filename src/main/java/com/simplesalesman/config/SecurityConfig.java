package com.simplesalesman.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
 * - Public access to web GUI endpoints (Thymeleaf templates)
 * - CORS support for local development
 *
 * Security considerations:
 * - Stateless security using Bearer tokens for API endpoints
 * - CSRF disabled for APIs (not needed for same-origin Thymeleaf)
 * - Supports granular role-based access via Keycloak realm roles
 * - CORS configured for local development (localhost:8080, localhost:8081)
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.4
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${simplesalesman.debug:false}")
    private boolean debugMode;

    /**
     * Configures HTTP security including authorization rules and JWT resource server.
     *
     * @param http the HttpSecurity builder
     * @return the configured SecurityFilterChain
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Initializing Security Filter Chain for SimpleSalesman (debug={})", debugMode);

        http
            // Disable CSRF for stateless REST APIs using JWT
            .csrf(csrf -> {
                csrf.disable();
                log.debug("CSRF protection disabled for REST API");
            })

            // Enable CORS for local development
            .cors(cors -> {
                cors.configurationSource(corsConfigurationSource());
                log.debug("CORS configuration applied");
            })

            // Define access rules for endpoints
            .authorizeHttpRequests(auth -> {
                auth
                    // Public endpoints - Web GUI (Thymeleaf templates)
                    .requestMatchers("/", "/gui/**", "/api-gui/**", "/index", "/health").permitAll()
                    
                    // Static resources (CSS, JS, images, favicon)
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/webjars/**").permitAll()
                    .requestMatchers("/favicon.ico", "/robots.txt", "/sitemap.xml").permitAll()
                    
                    // Spring Boot actuator endpoints (public for monitoring)
                    .requestMatchers("/actuator/health/**", "/actuator/info/**").permitAll()
                    .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                    
                    // Spring Boot error page
                    .requestMatchers("/error").permitAll()
                    
                    // Public API endpoints
                    .requestMatchers("/api/v1/import").permitAll()
                    
                    // Development endpoints (only in debug mode)
                    .requestMatchers("/api/v1/test/**").access((authentication, context) -> {
                        if (debugMode) {
                            log.debug("Debug mode: allowing access to test endpoints");
                            return new org.springframework.security.authorization.AuthorizationDecision(true);
                        }
                        return new org.springframework.security.authorization.AuthorizationDecision(false);
                    })
                    
                    // Protected API endpoints - require authentication
                    .requestMatchers("/api/**").authenticated()
                    
                    // Protected actuator endpoints - require authentication
                    .requestMatchers("/actuator/**").authenticated()
                    
                    // All other requests allowed (for Thymeleaf resources, etc.)
                    .anyRequest().permitAll();

                log.debug("Authorization rules set: Web GUI public, /api/v1/import public, /api/** requires authentication");
            })

            // Enable OAuth2 resource server with JWT validation
            .oauth2ResourceServer(oauth2 -> {
                oauth2.jwt(jwt -> {
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
                    log.debug("JWT Authentication Converter registered");
                });
                log.info("OAuth2 Resource Server with JWT enabled");
            })

            // Enhanced error handling for development
            .exceptionHandling(exceptions -> {
                exceptions.authenticationEntryPoint((request, response, authException) -> {
                    log.debug("Authentication failed for request: {} - {}", 
                             request.getRequestURI(), authException.getMessage());
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Authentication required\",\"message\":\"" + 
                                             authException.getMessage() + "\"}");
                });
            });

        log.info("Security Filter Chain successfully configured with CORS support");
        return http.build();
    }

    /**
     * CORS configuration for local development.
     * Allows requests from localhost:8080 (Keycloak) and localhost:8081 (application).
     */
    @Bean
    @Profile({"dev", "local", "default"})
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow specific origins for local development
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",
            "http://127.0.0.1:*"
        ));
        
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        log.info("CORS configured for local development - allowing localhost origins");
        return source;
    }

    /**
     * Production CORS configuration - more restrictive.
     */
    @Bean
    @Profile("prod")
    public CorsConfigurationSource prodCorsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configure specific allowed origins for production
        // configuration.setAllowedOrigins(Arrays.asList("https://yourdomain.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", "Content-Type", "X-Requested-With"
        ));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        
        log.info("Production CORS configured - restrictive settings");
        return source;
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

        if (debugMode) {
            log.info("Processing JWT for user: username='{}', sub='{}', email='{}', name='{}'",
                    username, subject, email, name);
        } else {
            log.debug("Processing JWT for user: username='{}', sub='{}'", username, subject);
        }

        // Attempt to extract realm roles
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");

        if (realmAccess != null && realmAccess.containsKey("roles")) {
            @SuppressWarnings("unchecked")
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");

            List<GrantedAuthority> authorities = roles.stream()
                .filter(role -> role != null && !role.trim().isEmpty())
                .map(role -> {
                    String authority = "ROLE_" + role.toUpperCase();
                    if (debugMode) {
                        log.debug("Mapping Keycloak role '{}' to authority '{}'", role, authority);
                    }
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toList());

            List<String> roleNames = roles.stream()
                .filter(role -> role != null && !role.trim().isEmpty())
                .map(String::toUpperCase)
                .toList();

            log.info("User '{}' authenticated with {} roles: {}",
                    username, roleNames.size(), roleNames);

            if (debugMode && log.isDebugEnabled()) {
                log.debug("JWT Exp: {}, Issued At: {}, Issuer: {}",
                        jwt.getExpiresAt(), jwt.getIssuedAt(), jwt.getIssuer());
                log.debug("Full JWT claims: {}", jwt.getClaims().keySet());
            }

            return authorities;
        }

        // No roles found
        log.warn("User '{}' (sub={}) has no realm_access.roles in JWT – access may be restricted",
                username, subject);

        if (debugMode && log.isDebugEnabled()) {
            log.debug("Available JWT claims: {}", jwt.getClaims().keySet());
            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
            if (resourceAccess != null) {
                log.debug("Resource access claims found: {}", resourceAccess.keySet());
            }
        }

        return Collections.emptyList();
    }
}
package com.simplesalesman.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Web controller for serving the SimpleSalesman API GUI.
 * 
 * This controller serves the Thymeleaf template that provides a web interface
 * for testing and interacting with the SimpleSalesman REST API.
 * 
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.6
 */
@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(WebController.class);

    @Value("${server.port:8081}")
    private String serverPort;

    @Value("${simplesalesman.keycloak.base-url:http://localhost:8080}")
    private String keycloakBaseUrl;

    @Value("${simplesalesman.keycloak.realm:simple-salesman-backend}")
    private String keycloakRealm;

    @Value("${simplesalesman.keycloak.client-id:simple-salesman-backend}")
    private String keycloakClientId;

    @Value("${simplesalesman.api.title:Simple Salesman API}")
    private String apiTitle;

    @Value("${simplesalesman.api.description:OAuth2 Authentication Flow}")
    private String apiDescription;

    @Value("${simplesalesman.defaults.address-id:121}")
    private Integer defaultAddressId;

    @Value("${simplesalesman.defaults.project-id:1}")
    private Integer defaultProjectId;

    @Value("${simplesalesman.defaults.note-id:1}")
    private Integer defaultNoteId;

    @Value("${simplesalesman.defaults.region:Linz}")
    private String defaultRegion;

    @Value("${simplesalesman.defaults.username:admin@admin.ad}")
    private String defaultUsername;

    @Value("${simplesalesman.defaults.note-text:Besucht, niemand angetroffen.}")
    private String defaultNoteText;

    @Value("${simplesalesman.defaults.update-text:Update: Besuch vereinbart}")
    private String defaultUpdateText;

    @Value("${simplesalesman.debug:false}")
    private Boolean debugMode;

    /**
     * Serves the main API GUI page.
     * 
     * @param model the Thymeleaf model
     * @param request the HTTP request
     * @return the template name
     */
    @GetMapping({"/", "/gui", "/api-gui"})
    public String apiGui(Model model, HttpServletRequest request) {
        log.debug("Serving API GUI page for request: {}", request.getRequestURI());

        // Determine base URLs
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        
        String baseUrl = scheme + "://" + serverName + ":" + port;
        String redirectUri = baseUrl + request.getRequestURI();

        // Check authentication status
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = isUserAuthenticated(authentication);
        String currentUsername = getCurrentUsername(authentication);

        // Add model attributes for Thymeleaf template
        model.addAttribute("pageTitle", "Simple Salesman API GUI - OAuth");
        model.addAttribute("apiTitle", apiTitle);
        model.addAttribute("apiDescription", apiDescription);
        
        // OAuth configuration
        model.addAttribute("apiHost", baseUrl);
        model.addAttribute("keycloakBaseUrl", keycloakBaseUrl);
        model.addAttribute("realm", keycloakRealm);
        model.addAttribute("clientId", keycloakClientId);
        model.addAttribute("redirectUri", redirectUri);
        
        // Authentication status
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("currentUsername", currentUsername);
        
        // Default values for form fields
        model.addAttribute("defaultAddressId", defaultAddressId);
        model.addAttribute("defaultProjectId", defaultProjectId);
        model.addAttribute("defaultNoteId", defaultNoteId);
        model.addAttribute("defaultRegion", defaultRegion);
        model.addAttribute("defaultUsername", currentUsername != null ? currentUsername : defaultUsername);
        model.addAttribute("defaultNoteText", defaultNoteText);
        model.addAttribute("defaultUpdateText", defaultUpdateText);
        
        // Debug mode
        model.addAttribute("debug", debugMode);

        log.info("API GUI served - Authenticated: {}, User: {}, Redirect URI: {}", 
                isAuthenticated, currentUsername, redirectUri);

        if (log.isDebugEnabled()) {
            log.debug("Model attributes: apiHost={}, keycloakBaseUrl={}, realm={}, clientId={}", 
                    baseUrl, keycloakBaseUrl, keycloakRealm, keycloakClientId);
        }

        if (!isAuthenticated) {
            // Optional: model.addAttribute(...) für Loginseite
            return "login"; // zeigt login.html
        }
        return "gui"; // zeigt gui.html (statt "index")
    }

    /**
     * Alternative endpoint for the API GUI.
     */
    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        return apiGui(model, request);
    }

    /**
     * Health check endpoint for the web interface.
     */
    @GetMapping("/health")
    public String health(Model model) {
        model.addAttribute("status", "UP");
        model.addAttribute("timestamp", java.time.Instant.now().toString());
        model.addAttribute("service", "SimpleSalesman Web GUI");
        return "health"; // Optional: create a simple health.html template
    }

    /**
     * Checks if the current user is authenticated via OAuth2 JWT.
     * 
     * @param authentication the current authentication
     * @return true if authenticated with a valid JWT
     */
    private boolean isUserAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Check if it's an OAuth2 JWT authentication
        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            // Additional validation could be added here
            return jwt.getExpiresAt() != null && jwt.getExpiresAt().isAfter(java.time.Instant.now());
        }

        return false;
    }

    /**
     * Extracts the username from the current authentication.
     * 
     * @param authentication the current authentication
     * @return the username or null if not available
     */
    private String getCurrentUsername(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String username = jwt.getClaimAsString("preferred_username");
            if (username == null) {
                username = jwt.getClaimAsString("email");
            }
            if (username == null) {
                username = jwt.getSubject();
            }
            return username;
        }

        return authentication.getName();
    }
}
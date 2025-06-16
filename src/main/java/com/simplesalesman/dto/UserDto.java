package com.simplesalesman.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Transfer Object (DTO) representing a user within the SimpleSalesman system.
 *
 * This object is used to transfer user identity and access role data between
 * the backend and frontend systems, including Keycloak integration details.
 *
 * Typically exposed via admin or identity-related APIs.
 *
 * Example:
 * {
 *   "id": 5,
 *   "keycloakId": "6bdfc1d3-92af-4b18-98a4-81298b34ef45",
 *   "username": "john.doe",
 *   "role": "SALES",
 *   "active": true
 * }
 *
 * Author: SimpleSalesman Team  
 * @version 0.0.5  
 * @since 0.0.3
 */
public class UserDto {

    private static final Logger logger = LoggerFactory.getLogger(UserDto.class);

    private Long id;
    private String keycloakId;
    private String username;
    private String role;
    private boolean active;

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        logger.debug("UserDto id set to {}", id);
        this.id = id;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        logger.debug("UserDto keycloakId set to '{}'", keycloakId);
        this.keycloakId = keycloakId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        logger.debug("UserDto username set to '{}'", username);
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        logger.debug("UserDto role set to '{}'", role);
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        logger.debug("UserDto active flag set to {}", active);
        this.active = active;
    }
}

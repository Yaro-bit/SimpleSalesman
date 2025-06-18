package com.simplesalesman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing a user within the SimpleSalesman system.
 *
 * Used to expose user identity, activation state and associated role information.
 * Typically returned via identity or admin-related endpoints.
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
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.3
 */
@Schema(description = "DTO representing an application user")
public class UserDto {

    private static final Logger logger = LoggerFactory.getLogger(UserDto.class);

    @Schema(description = "Internal numeric user ID", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "UUID from Keycloak", example = "6bdfc1d3-92af-4b18-98a4-81298b34ef45")
    private String keycloakId;

    @Schema(description = "Username used for login", example = "john.doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username must not be blank")
    @Size(max = 100, message = "Username must not exceed 100 characters")
    private String username;

    @Schema(description = "Assigned role", example = "SALES", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Role must not be blank")
    private String role;

    @Schema(description = "Whether the user is active", example = "true")
    private boolean active;


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


    @Override
    public String toString() {
        return String.format("UserDto{id=%d, username='%s', role='%s', active=%s}",
                id, username, role, active);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return active == userDto.active &&
                Objects.equals(id, userDto.id) &&
                Objects.equals(keycloakId, userDto.keycloakId) &&
                Objects.equals(username, userDto.username) &&
                Objects.equals(role, userDto.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keycloakId, username, role, active);
    }
}

package com.simplesalesman.entity;

import jakarta.persistence.*;

/**
 * JPA Entity representing an application user in the SimpleSalesman
 * application.
 *
 * This entity holds user-specific data such as the linked Keycloak ID,
 * username, assigned role (e.g., "ADMIN", "USER"), and activation status. It is
 * used for authentication, authorization, and frontend personalization.
 *
 * Key characteristics: - Each user is uniquely identified via their Keycloak
 * UUID - Roles are stored as plain strings to simplify integration - Activation
 * flag supports user-based access control
 *
 * Constraints: - keycloakId must be unique and not null
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.1
 */
@Entity
@Table(name = "app_user")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String keycloakId; // UUID von Keycloak

	private String username;

	private String role; // z. B. "ADMIN", "USER"

	private boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeycloakId() {
		return keycloakId;
	}

	public void setKeycloakId(String keycloakId) {
		this.keycloakId = keycloakId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AppUser))
			return false;
		AppUser other = (AppUser) o;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}

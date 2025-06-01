package com.simplesalesman.dto;

public class UserDto {

    private Long id;
    private String keycloakId;
    private String username;
    private String role;
    private boolean active;
    
    // Getter und Setter
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


    
    
}

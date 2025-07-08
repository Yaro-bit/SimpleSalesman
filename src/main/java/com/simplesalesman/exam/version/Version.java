// 1. Entity Class (Version.java)
package com.simplesalesman.exam.version;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "t_version")
public class Version {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "Version cannot be empty")
    @Size(max = 255, message = "Version cannot exceed 255 characters")
    private String version;
    
    // Default constructor
    public Version() {}
    
    // Constructor
    public Version(String version) {
        this.version = version;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Version{" +
                "id=" + id +
                ", version='" + version + '\'' +
                '}';
    }
}

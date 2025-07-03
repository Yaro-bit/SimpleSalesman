// 1. Entity Class (Sample.java)
package com.simplesalesman.exam.sample;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Generic Sample entity - copy this package and rename for any new CRUD table.
 */
@Entity
@Table(name = "t_sample")
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;
    
    // Constructors
    public Sample() {}
    
    public Sample(String name) {
        this.name = name;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Sample{id=" + id + ", name='" + name + "'}";
    }
}
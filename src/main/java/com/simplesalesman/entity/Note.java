package com.simplesalesman.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing a note attached to an address in the SimpleSalesman application.
 *
 * Each note stores a free-text comment created by a sales user, along with metadata such as
 * timestamp and author. Notes are used to track visits, customer feedback, or reminders.
 *
 * Relationships:
 * - ManyToOne: Address (each note belongs to one address)
 *
 * Constraints:
 * - Maximum text length: 2000 characters
 * - createdAt and createdBy must be set when the note is created (typically by the backend)
 *
 * Example use case:
 * - A user visits an address and logs feedback like "Customer interested, follow-up next week"
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.1
 */
@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String text;

    private LocalDateTime createdAt;

    private String createdBy; // z. B. Verkäufername oder User-ID

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    // Getter und Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    // Optional: equals & hashCode (nur wenn gewünscht)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note other = (Note) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

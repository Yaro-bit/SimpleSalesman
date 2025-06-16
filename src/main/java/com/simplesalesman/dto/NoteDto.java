package com.simplesalesman.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a note attached to an address.
 *
 * Notes are created by sales team members during or after D2D visits and
 * are associated with a specific address entity.
 *
 * This object is used to transfer note data between frontend and backend.
 *
 * Author: SimpleSalesman Team  
 * @version 0.0.5  
 * @since 0.0.3
 */
public class NoteDto {

    private static final Logger logger = LoggerFactory.getLogger(NoteDto.class);

    /**
     * Unique identifier of the note.
     */
    private Long id;

    /**
     * The textual content of the note.
     */
    private String text;

    /**
     * Timestamp when the note was created.
     */
    private LocalDateTime createdAt;

    /**
     * Username of the person who created the note.
     */
    private String createdBy;

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        logger.debug("NoteDto ID set to {}", id);
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        logger.debug("NoteDto text set to '{}'", text != null ? text : "null");
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        logger.debug("NoteDto createdAt set to {}", createdAt);
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        logger.debug("NoteDto createdBy set to '{}'", createdBy);
        this.createdBy = createdBy;
    }
}

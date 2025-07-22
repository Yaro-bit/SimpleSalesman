package com.simplesalesman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing a note attached to an address in the SimpleSalesman system.
 * 
 * Notes are typically created by field agents during or after door-to-door visits,
 * and linked to specific address records for historical context.
 * 
 * This object is used for transferring note data between frontend and backend layers.
 *
 * @author SimpleSalesman Team
 * @version 0.1.0
 * @since 0.0.4
 */
@Schema(description = "Note attached to an address")
public class NoteDto {

    private static final Logger logger = LoggerFactory.getLogger(NoteDto.class);

    @Schema(description = "Unique identifier of the note", example = "17", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Text content of the note", example = "Customer asked for callback next week", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Note text must not be blank")
    @Size(max = 5000, message = "Note text must not exceed 5000 characters")
    private String text;

    @Schema(description = "Timestamp of when the note was created", example = "2025-06-18T09:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Username of the creator", example = "john.doe")
    private String createdBy;

    @Schema(description = "ID of the associated address", example = "42")
    private Long addressId;

    @Schema(description = "Address information for display purposes", example = "1234 Main Street 5")
    private String addressText;



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
        logger.debug("NoteDto text set to '{}'", text != null ? text : "<null>");
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
        logger.debug("NoteDto createdBy set to '{}'", createdBy != null ? createdBy : "<null>");
        this.createdBy = createdBy;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        logger.debug("NoteDto addressId set to {}", addressId);
        this.addressId = addressId;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        logger.debug("NoteDto addressText set to '{}'", addressText != null ? addressText : "<null>");
        this.addressText = addressText;
    }



    @Override
    public String toString() {
        return String.format("NoteDto{id=%d, createdBy='%s', createdAt=%s, addressId=%d, addressText='%s', text='%s'}",
                id, createdBy, createdAt, addressId, addressText, text != null ? text : "<null>");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteDto)) return false;
        NoteDto noteDto = (NoteDto) o;
        return Objects.equals(id, noteDto.id) &&
                Objects.equals(text, noteDto.text) &&
                Objects.equals(createdAt, noteDto.createdAt) &&
                Objects.equals(createdBy, noteDto.createdBy) &&
                Objects.equals(addressId, noteDto.addressId) &&
                Objects.equals(addressText, noteDto.addressText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, createdAt, createdBy, addressId, addressText);
    }
}
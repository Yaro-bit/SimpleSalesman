package com.simplesalesman.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) for address information in the SimpleSalesman system.
 *
 * This class represents an address and includes related projects and notes.
 * It is used to transfer address-related data between the client and server
 * and includes validation annotations for safe input handling.
 *
 * Logging is included for traceability and debug support.
 *
 * Author: SimpleSalesman Team
 * @version 0.0.5
 * @since 0.0.5
 */
@Schema(description = "Address information including linked projects and notes")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {

    private static final Logger log = LoggerFactory.getLogger(AddressDto.class);

    @Schema(description = "Unique ID of the address", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("id")
    private Long id;

    @Schema(description = "Full address text", example = "Main Street 123, 1010 Vienna", required = true)
    @JsonProperty("addressText")
    @NotBlank(message = "Address text must not be blank")
    @Size(min = 5, max = 500, message = "Address text must be between 5 and 500 characters")
    private String addressText;

    @Schema(description = "Name of the region or area", example = "Upper Austria")
    @JsonProperty("regionName")
    @Size(max = 100, message = "Region name must not exceed 100 characters")
    private String regionName;

    @Schema(description = "List of projects linked to this address")
    @JsonProperty("projects")
    @Valid
    private List<ProjectDto> projects;

    @Schema(description = "List of notes associated with this address")
    @JsonProperty("notes")
    @Valid
    private List<NoteDto> notes;

    // === Constructors ===

    public AddressDto() {
        log.trace("New AddressDto instance created");
    }

    public AddressDto(String addressText, String regionName) {
        this.addressText = addressText;
        this.regionName = regionName;
        log.debug("AddressDto created with addressText='{}', regionName='{}'", addressText, regionName);
    }

    public AddressDto(Long id, String addressText, String regionName,
                      List<ProjectDto> projects, List<NoteDto> notes) {
        this.id = id;
        this.addressText = addressText;
        this.regionName = regionName;
        this.projects = projects;
        this.notes = notes;
        log.debug("Full AddressDto created: id={}, addressText='{}', regionName='{}', projects={}, notes={}",
                id, addressText, regionName,
                projects != null ? projects.size() : 0,
                notes != null ? notes.size() : 0);
    }

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        log.trace("AddressDto ID changed from {} to {}", this.id, id);
        this.id = id;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        if (addressText != null && addressText.trim().isEmpty()) {
            log.warn("Attempted to set blank addressText");
            throw new IllegalArgumentException("Address text must not be blank");
        }
        log.debug("AddressDto addressText changed from '{}' to '{}'", this.addressText, addressText);
        this.addressText = addressText;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        log.debug("AddressDto regionName changed from '{}' to '{}'", this.regionName, regionName);
        this.regionName = regionName;
    }

    public List<ProjectDto> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDto> projects) {
        int oldCount = this.projects != null ? this.projects.size() : 0;
        int newCount = projects != null ? projects.size() : 0;
        log.debug("AddressDto projects updated: {} → {} projects", oldCount, newCount);
        this.projects = projects;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDto> notes) {
        int oldCount = this.notes != null ? this.notes.size() : 0;
        int newCount = notes != null ? notes.size() : 0;
        log.debug("AddressDto notes updated: {} → {} notes", oldCount, newCount);
        this.notes = notes;
    }

    // === Utility Methods ===

    public boolean hasProjects() {
        return projects != null && !projects.isEmpty();
    }

    public boolean hasNotes() {
        return notes != null && !notes.isEmpty();
    }

    public int getProjectCount() {
        return projects != null ? projects.size() : 0;
    }

    public int getNoteCount() {
        return notes != null ? notes.size() : 0;
    }

    // === Object Overrides ===

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AddressDto that = (AddressDto) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(addressText, that.addressText) &&
                Objects.equals(regionName, that.regionName) &&
                Objects.equals(projects, that.projects) &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, addressText, regionName, projects, notes);
    }

    @Override
    public String toString() {
        return String.format("AddressDto{id=%d, addressText='%s', regionName='%s', projectCount=%d, noteCount=%d}",
                id, addressText, regionName, getProjectCount(), getNoteCount());
    }
}

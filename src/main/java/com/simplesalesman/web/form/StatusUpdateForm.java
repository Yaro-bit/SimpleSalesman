package com.simplesalesman.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Form DTO für Projekt-Status-Updates in der Web-Oberfläche.
 * 
 * Optimiert für HTML-Dropdown-Auswahl mit Validierung.
 */
public class StatusUpdateForm {

    @NotNull(message = "Projekt-ID ist erforderlich")
    private Long projectId;

    @NotBlank(message = "Neuer Status ist erforderlich")
    private String newStatus;

    private String currentStatus; // Nur zur Anzeige

    // === Constructors ===

    public StatusUpdateForm() {
    }

    public StatusUpdateForm(Long projectId, String newStatus) {
        this.projectId = projectId;
        this.newStatus = newStatus;
    }

    // === Getters & Setters ===

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    // === Utility Methods ===

    public boolean isStatusChanging() {
        return currentStatus != null && newStatus != null && !currentStatus.equals(newStatus);
    }

    @Override
    public String toString() {
        return String.format("StatusUpdateForm{projectId=%d, currentStatus='%s', newStatus='%s'}", 
                           projectId, currentStatus, newStatus);
    }
}
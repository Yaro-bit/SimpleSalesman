package com.simplesalesman.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Transfer Object (DTO) for updating the status of a project.
 *
 * Used in API requests where a project’s current status (e.g., OPEN, IN_PROGRESS, COMPLETED)
 * needs to be updated by the user.
 *
 * Example payload:
 * {
 *   "projectId": 12,
 *   "newStatus": "COMPLETED"
 * }
 *
 * Author: SimpleSalesman Team
 * @version 0.0.5
 * @since 0.0.3
 */
public class StatusUpdateDto {

    private static final Logger logger = LoggerFactory.getLogger(StatusUpdateDto.class);

    /**
     * The ID of the project to be updated.
     */
    private Long projectId;

    /**
     * The new status to assign to the project.
     */
    private String newStatus;

    // === Getters and Setters ===

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        logger.debug("StatusUpdateDto projectId set to {}", projectId);
        this.projectId = projectId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        logger.debug("StatusUpdateDto newStatus set to '{}'", newStatus);
        this.newStatus = newStatus;
    }
}

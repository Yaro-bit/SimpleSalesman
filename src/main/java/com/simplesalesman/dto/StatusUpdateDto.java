package com.simplesalesman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) for updating the status of a project in the SimpleSalesman system.
 *
 * Typically used in PATCH or PUT API requests to update a project's current lifecycle status.
 *
 * Example payload:
 * {
 *   "projectId": 12,
 *   "newStatus": "COMPLETED"
 * }
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.3
 */
@Schema(description = "DTO for updating the status of a project")
public class StatusUpdateDto {

    private static final Logger logger = LoggerFactory.getLogger(StatusUpdateDto.class);

    @Schema(description = "Unique identifier of the project", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Project ID must not be null")
    private Long projectId;

    @Schema(description = "New status to assign to the project", example = "COMPLETED", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "New status must not be blank")
    private String newStatus;



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



    @Override
    public String toString() {
        return String.format("StatusUpdateDto{projectId=%d, newStatus='%s'}", projectId, newStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusUpdateDto)) return false;
        StatusUpdateDto that = (StatusUpdateDto) o;
        return Objects.equals(projectId, that.projectId) &&
                Objects.equals(newStatus, that.newStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, newStatus);
    }
}

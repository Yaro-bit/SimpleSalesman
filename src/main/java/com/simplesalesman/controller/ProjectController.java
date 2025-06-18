package com.simplesalesman.controller;

import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.dto.StatusUpdateDto;
import com.simplesalesman.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for handling project-related operations in the SimpleSalesman application.
 *
 * Provides endpoints to:
 * - Retrieve all projects or a specific one by ID
 * - Get the status of a project
 * - Update a project's status
 *
 * This controller supports the core functionality needed by sales staff to track the progress
 * of construction or sales-related projects.
 *
 * API Endpoints:
 * - GET /api/v1/projects                   → List all projects
 * - GET /api/v1/projects/{id}             → Get project details by ID
 * - GET /api/v1/projects/{id}/status      → Get project status
 * - PATCH /api/v1/projects/status         → Update project status
 *
 * Security:
 * - Assumes authenticated access via JWT
 * - Status updates are restricted to authorized roles (enforced in the service or via method-level security)
 *
 * @Author SimpleSalesman Team
 * @Version 0.0.5
 * @Since 0.0.1
 */
@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    /**
     * Constructor for dependency injection.
     *
     * @param projectService the service that handles project-related logic
     */
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
        logger.info("ProjectController initialized");
    }

    /**
     * Retrieves all available projects from the system.
     *
     * @return HTTP 200 with a list of ProjectDto
     */
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        logger.info("GET /projects called – fetching all projects");
        List<ProjectDto> projects = projectService.getAllProjects();
        logger.debug("Retrieved {} projects", projects.size());
        return ResponseEntity.ok(projects);
    }

    /**
     * Retrieves details of a specific project by its ID.
     *
     * @param id the project ID
     * @return HTTP 200 with the ProjectDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        logger.info("GET /projects/{} called – fetching project by ID", id);
        ProjectDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    /**
     * Returns the current status of a specific project.
     *
     * @param id the project ID
     * @return HTTP 200 with a string representing the project status
     */
    @GetMapping("/{id}/status")
    public ResponseEntity<String> getProjectStatus(@PathVariable Long id) {
        logger.info("GET /projects/{}/status called – retrieving status", id);
        String status = projectService.getProjectStatusById(id);
        logger.debug("Status for project {}: {}", id, status);
        return ResponseEntity.ok(status);
    }

    /**
     * Updates the status of a given project.
     *
     * @param statusUpdateDto object containing project ID and new status
     * @return HTTP 204 No Content on success
     */
    @PatchMapping("/status")
    public ResponseEntity<Void> updateProjectStatus(@RequestBody StatusUpdateDto statusUpdateDto) {
        logger.info("PATCH /projects/status called – updating status for project ID {}", statusUpdateDto.getProjectId());
        projectService.updateStatus(statusUpdateDto);
        logger.debug("Project ID {} status updated to '{}'", statusUpdateDto.getProjectId(), statusUpdateDto.getNewStatus());
        return ResponseEntity.noContent().build();
    }
}

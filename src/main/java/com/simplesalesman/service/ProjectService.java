package com.simplesalesman.service;

import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.dto.StatusUpdateDto;
import com.simplesalesman.entity.Project;
import com.simplesalesman.exception.ProjectNotFoundException;
import com.simplesalesman.mapper.ProjectMapper;
import com.simplesalesman.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing project-related business logic in the SimpleSalesman application.
 *
 * This service handles the retrieval and updating of {@link Project} entities,
 * including converting between entities and {@link ProjectDto}s, and changing project status.
 *
 * Key Responsibilities:
 * - List all projects in the system
 * - Retrieve specific projects by ID
 * - Read and update project status
 *
 * Error Handling:
 * - Throws {@link ProjectNotFoundException} when a project is not found
 *
 * Dependencies:
 * - {@link ProjectRepository} for persistence operations
 * - {@link ProjectMapper} for entity <-> DTO mapping
 *
 * Usage:
 * - Called by {@code ProjectController} to expose REST endpoints
 *
 * Author: SimpleSalesman Team  
 * @version 0.0.6  
 * @since 0.0.5
 */
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    /**
     * Retrieves all projects in the system.
     *
     * @return list of all projects as DTOs
     */
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single project by its ID.
     *
     * @param id the ID of the project
     * @return ProjectDto with detailed information
     * @throws ProjectNotFoundException if project is not found
     */
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Projekt mit ID " + id + " nicht gefunden"));
        return projectMapper.toDto(project);
    }

    /**
     * Retrieves the current status of a project by its ID.
     *
     * @param id the ID of the project
     * @return the status string of the project
     * @throws ProjectNotFoundException if project is not found
     */
    public String getProjectStatusById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Projekt mit ID " + id + " nicht gefunden"));
        return project.getStatus();
    }

    /**
     * Updates the status field of a project.
     *
     * @param statusUpdateDto contains project ID and new status value
     * @throws ProjectNotFoundException if project is not found
     */
    public void updateStatus(StatusUpdateDto statusUpdateDto) {
        Project project = projectRepository.findById(statusUpdateDto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Projekt mit ID " + statusUpdateDto.getProjectId() + " nicht gefunden"));

        project.setStatus(statusUpdateDto.getNewStatus());
        projectRepository.save(project);
    }
}

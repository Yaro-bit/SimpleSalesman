package com.simplesalesman.controller;

import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.dto.StatusUpdateDto;
import com.simplesalesman.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    // GET /api/v1/projects/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        ProjectDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    // GET /api/v1/projects/{id}/status
    @GetMapping("/{id}/status")
    public ResponseEntity<String> getProjectStatus(@PathVariable Long id) {
        String status = projectService.getProjectStatusById(id);
        return ResponseEntity.ok(status);
    }

    @PatchMapping("/status")
    public ResponseEntity<Void> updateProjectStatus(@RequestBody StatusUpdateDto statusUpdateDto) {
        projectService.updateStatus(statusUpdateDto);
        return ResponseEntity.noContent().build();
    }
}

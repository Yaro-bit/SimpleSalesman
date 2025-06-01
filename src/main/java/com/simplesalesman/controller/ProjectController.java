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

    @PatchMapping("/status")
    public ResponseEntity<Void> updateProjectStatus(@RequestBody StatusUpdateDto statusUpdateDto) {
        projectService.updateStatus(statusUpdateDto);
        return ResponseEntity.noContent().build();
    }
}

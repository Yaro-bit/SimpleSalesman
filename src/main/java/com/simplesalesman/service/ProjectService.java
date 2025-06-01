package com.simplesalesman.service;

import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.dto.StatusUpdateDto;
import com.simplesalesman.entity.Project;
import com.simplesalesman.mapper.ProjectMapper;
import com.simplesalesman.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    public void updateStatus(StatusUpdateDto statusUpdateDto) {
        Project project = projectRepository.findById(statusUpdateDto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Projekt nicht gefunden"));

        project.setStatus(statusUpdateDto.getNewStatus());
        projectRepository.save(project);
    }

    // Optional: Methoden für Projektanlage, Löschung, Filterung etc.
}

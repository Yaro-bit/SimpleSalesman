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

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream().map(projectMapper::toDto).collect(Collectors.toList());
    }

    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Projekt mit ID " + id + " nicht gefunden"));
        return projectMapper.toDto(project);
    }

    public String getProjectStatusById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Projekt mit ID " + id + " nicht gefunden"));
        return project.getStatus();
    }

    public void updateStatus(StatusUpdateDto statusUpdateDto) {
        Project project = projectRepository.findById(statusUpdateDto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Projekt mit ID " + statusUpdateDto.getProjectId() + " nicht gefunden"));

        project.setStatus(statusUpdateDto.getNewStatus());
        projectRepository.save(project);
    }
}

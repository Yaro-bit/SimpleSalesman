package com.simplesalesman.service;

import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.dto.StatusUpdateDto;
import com.simplesalesman.entity.Project;
import com.simplesalesman.mapper.ProjectMapper;
import com.simplesalesman.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectMapper projectMapper;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectMapper = mock(ProjectMapper.class);
        projectService = new ProjectService(projectRepository, projectMapper);
    }

    @Test
    @DisplayName("getAllProjects: Gibt alle Projekte als DTO-Liste zurück")
    void getAllProjects_returnsAllProjectsAsDtos() {
        Project p1 = new Project();
        p1.setId(1L);
        Project p2 = new Project();
        p2.setId(2L);

        ProjectDto dto1 = new ProjectDto();
        dto1.setId(1L);
        ProjectDto dto2 = new ProjectDto();
        dto2.setId(2L);

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));
        when(projectMapper.toDto(p1)).thenReturn(dto1);
        when(projectMapper.toDto(p2)).thenReturn(dto2);

        List<ProjectDto> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        verify(projectRepository, times(1)).findAll();
        verify(projectMapper, times(1)).toDto(p1);
        verify(projectMapper, times(1)).toDto(p2);
    }

    @Test
    @DisplayName("getAllProjects: Gibt leere Liste zurück wenn keine Projekte vorhanden sind")
    void getAllProjects_returnsEmptyListIfNoProjects() {
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProjectDto> result = projectService.getAllProjects();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(projectRepository, times(1)).findAll();
        verifyNoInteractions(projectMapper);
    }

    @Test
    @DisplayName("updateStatus: Setzt neuen Status und speichert das Projekt")
    void updateStatus_setsStatusAndSavesProject() {
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto();
        statusUpdateDto.setProjectId(99L);
        statusUpdateDto.setNewStatus("IN_PROGRESS");

        Project project = new Project();
        project.setId(99L);
        project.setStatus("OPEN");

        when(projectRepository.findById(99L)).thenReturn(Optional.of(project));

        projectService.updateStatus(statusUpdateDto);

        assertEquals("IN_PROGRESS", project.getStatus());
        verify(projectRepository, times(1)).findById(99L);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("updateStatus: Wirft Exception wenn Projekt nicht gefunden wird")
    void updateStatus_throwsExceptionIfProjectNotFound() {
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto();
        statusUpdateDto.setProjectId(123L);
        statusUpdateDto.setNewStatus("DONE");

        when(projectRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            projectService.updateStatus(statusUpdateDto);
        });

        assertEquals("Projekt nicht gefunden", ex.getMessage());
        verify(projectRepository, times(1)).findById(123L);
        verify(projectRepository, never()).save(any());
    }
}

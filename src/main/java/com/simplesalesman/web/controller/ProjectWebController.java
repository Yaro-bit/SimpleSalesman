package com.simplesalesman.web.controller;

import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.dto.StatusUpdateDto;
import com.simplesalesman.service.ProjectService;
import com.simplesalesman.web.form.StatusUpdateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

/**
 * MVC Controller für Projektverwaltung in der Web-Oberfläche.
 * 
 * Stellt HTML-Views für Projektanzeige und Status-Updates bereit.
 */
@Controller
@RequestMapping("/web/projects")
public class ProjectWebController {

    private static final Logger log = LoggerFactory.getLogger(ProjectWebController.class);
    private final ProjectService projectService;

    public ProjectWebController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Zeigt alle Projekte in einer Tabelle an.
     */
    @GetMapping
    public String listProjects(Model model) {
        log.info("Loading projects list view");
        List<ProjectDto> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        
        // Für Status-Update-Dropdown
        model.addAttribute("statusUpdateForm", new StatusUpdateForm());
        model.addAttribute("availableStatuses", getAvailableStatuses());
        
        return "projects/list";
    }

    /**
     * Zeigt Details eines einzelnen Projekts.
     */
    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        log.info("Loading project details for ID: {}", id);
        try {
            ProjectDto project = projectService.getProjectById(id);
            model.addAttribute("project", project);
            
            // Für Status-Update
            StatusUpdateForm statusForm = new StatusUpdateForm();
            statusForm.setProjectId(id);
            statusForm.setCurrentStatus(project.getStatus());
            model.addAttribute("statusUpdateForm", statusForm);
            model.addAttribute("availableStatuses", getAvailableStatuses());
            
            return "projects/detail";
        } catch (Exception e) {
            log.error("Error loading project details for ID: {}", id, e);
            return "redirect:/web/projects?error=notfound";
        }
    }

    /**
     * Aktualisiert den Status eines Projekts.
     */
    @PostMapping("/status")
    public String updateProjectStatus(@Valid @ModelAttribute StatusUpdateForm statusForm,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.warn("Status update form validation failed: {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", "Ungültige Eingabe!");
            return "redirect:/web/projects";
        }

        try {
            StatusUpdateDto dto = new StatusUpdateDto();
            dto.setProjectId(statusForm.getProjectId());
            dto.setNewStatus(statusForm.getNewStatus());
            
            projectService.updateStatus(dto);
            log.info("Project status updated: ID={}, newStatus={}", 
                    statusForm.getProjectId(), statusForm.getNewStatus());
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Projektstatus erfolgreich aktualisiert!");
            
            return "redirect:/web/projects/" + statusForm.getProjectId();
        } catch (Exception e) {
            log.error("Error updating project status", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Fehler beim Aktualisieren des Status: " + e.getMessage());
            return "redirect:/web/projects";
        }
    }

    /**
     * Liefert verfügbare Status-Optionen.
     */
    private String[] getAvailableStatuses() {
        return new String[]{
            "OPEN",
            "IN_PROGRESS", 
            "COMPLETED",
            "ON_HOLD",
            "CANCELLED"
        };
    }
}
package com.simplesalesman.controller;

import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.dto.StatusUpdateDto;
import com.simplesalesman.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Testet, ob der GET-Endpoint alle Projekte korrekt zurückgibt.
     * Erwartet: HTTP 200 und Liste von ProjectDto im JSON-Format.
     */
    @Test
    @DisplayName("GET /api/v1/projects gibt alle Projekte mit allen Feldern zurück")
    void getAllProjects_returnsProjectList() throws Exception {
        ProjectDto project1 = new ProjectDto();
        project1.setId(1L);
        project1.setStatus("AKTIV");
        project1.setOperator("A1 Telekom");
        project1.setConstructionCompany("Porr AG");
        project1.setPlannedConstructionEnd(LocalDate.of(2025, 12, 31));
        project1.setConstructionCompleted(true);
        project1.setSalesStart(LocalDate.of(2025, 5, 1));
        project1.setSalesEnd(LocalDate.of(2025, 7, 31));
        project1.setNumberOfHomes(15);
        project1.setContractPresent(true);
        project1.setCommissionCategory("Kategorie A");
        project1.setKgNumber("KG-4711");
        project1.setProductPrice(new BigDecimal("2499.99"));
        project1.setOutdoorFeePresent(false);

        Mockito.when(projectService.getAllProjects()).thenReturn(Arrays.asList(project1));

        mockMvc.perform(get("/api/v1/projects")
                .with(jwt())) // JWT für Auth!
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("AKTIV"))
                .andExpect(jsonPath("$[0].operator").value("A1 Telekom"))
                .andExpect(jsonPath("$[0].constructionCompany").value("Porr AG"))
                .andExpect(jsonPath("$[0].plannedConstructionEnd").value("2025-12-31"))
                .andExpect(jsonPath("$[0].constructionCompleted").value(true))
                .andExpect(jsonPath("$[0].salesStart").value("2025-05-01"))
                .andExpect(jsonPath("$[0].salesEnd").value("2025-07-31"))
                .andExpect(jsonPath("$[0].numberOfHomes").value(15))
                .andExpect(jsonPath("$[0].contractPresent").value(true))
                .andExpect(jsonPath("$[0].commissionCategory").value("Kategorie A"))
                .andExpect(jsonPath("$[0].kgNumber").value("KG-4711"))
                .andExpect(jsonPath("$[0].productPrice").value(2499.99))
                .andExpect(jsonPath("$[0].outdoorFeePresent").value(false));
    }

    /**
     * Testet den PATCH-Endpoint für die Statusaktualisierung.
     * Erwartet: HTTP 204 No Content, Service-Methode wird aufgerufen.
     */
    @Test
    @DisplayName("PATCH /api/v1/projects/status aktualisiert Projektstatus")
    void updateProjectStatus_returnsNoContent() throws Exception {
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto();
        statusUpdateDto.setProjectId(1L);
        statusUpdateDto.setNewStatus("Fertig");

        Mockito.doNothing().when(projectService).updateStatus(any(StatusUpdateDto.class));

        mockMvc.perform(patch("/api/v1/projects/status")
                .with(jwt()) // JWT für Auth!
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdateDto)))
                .andExpect(status().isNoContent());

        Mockito.verify(projectService).updateStatus(any(StatusUpdateDto.class));
    }
}

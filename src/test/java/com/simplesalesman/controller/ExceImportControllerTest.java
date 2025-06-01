package com.simplesalesman.controller;

import com.simplesalesman.dto.ImportResultDto;
import com.simplesalesman.service.ExcelImportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Testklasse für ImportController.
 * 
 * Testet den Upload einer Excel-Datei über den API-Endpunkt und prüft,
 * ob der Controller den erfolgreichen Response korrekt zurückgibt.
 * 
 * Nutzt MockMvc für die HTTP-Simulation und mockt ExcelImportService.
 */
@WebMvcTest(controllers = ImportController.class)
class ImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mockt den ExcelImportService, damit keine echte Logik oder Datenbank benötigt wird.
     */
    @SuppressWarnings("removal")
	@MockBean
    private ExcelImportService excelImportService;

    /**
     * Testet, ob ein Datei-Upload per Multipart-Request erfolgreich verarbeitet wird.
     * 
     * Erwartet:
     * - HTTP 200 OK
     * - JSON-Response mit success=true und recordsProcessed=1
     *
     * Vorgehen:
     * - Service wird so gemockt, dass ein erfolgreicher Import simuliert wird.
     * - Mit MockMultipartFile wird eine Dummy-Excel-Datei erzeugt.
     * - Multipart-Request an /api/v1/import wird simuliert und die Antwort geprüft.
     */
    @Test
    void importExcel_returnsOk_whenFileUploaded() throws Exception {
        // Service simuliert erfolgreichen Import
        ImportResultDto dto = new ImportResultDto();
        dto.setSuccess(true);
        dto.setRecordsProcessed(1);

        Mockito.when(excelImportService.importExcel(any())).thenReturn(dto);

        // Dummy-Datei für Upload simulieren
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new byte[]{1,2,3}
        );

        // Simuliere Multipart-Request und prüfe Response
        mockMvc.perform(multipart("/api/v1/import")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.recordsProcessed").value(1));
    }
}

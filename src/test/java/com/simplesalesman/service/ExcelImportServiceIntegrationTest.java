package com.simplesalesman.service;

import com.simplesalesman.dto.ImportResultDto;
import com.simplesalesman.repository.AddressRepository;
import com.simplesalesman.repository.ProjectRepository;
import com.simplesalesman.repository.RegionRepository;
import com.simplesalesman.util.ExcelUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Optional: Nur wenn du ein spezielles Test-Profil nutzt
class ExcelImportServiceIntegrationTest {

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("Importiere echte Excel-Datei und prüfe Ergebnis")
    void importExcel_realExcelFile_success() throws Exception {
        
    	// Statt FileInputStream → Resource Stream!
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("test_import.xlsx")) {
            assertNotNull(is, "Die Testdatei test_import.xlsx wurde nicht gefunden!");

            MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_import.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                is
            );

            ImportResultDto result = excelImportService.importExcel(file);

            assertTrue(result.isSuccess(), "Import sollte erfolgreich sein");
            assertTrue(result.getRecordsProcessed() > 0, "Es sollten Datensätze importiert werden");
            assertTrue(result.getErrors().isEmpty(), "Es sollten keine Fehler auftreten");
        }
    }
}

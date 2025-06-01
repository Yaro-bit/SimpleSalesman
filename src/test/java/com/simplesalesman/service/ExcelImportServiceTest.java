package com.simplesalesman.service;

import com.simplesalesman.dto.ImportResultDto;
import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Project;
import com.simplesalesman.entity.Region;
import com.simplesalesman.repository.AddressRepository;
import com.simplesalesman.repository.ProjectRepository;
import com.simplesalesman.repository.RegionRepository;
import com.simplesalesman.util.ExcelUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testklasse für ExcelImportService.
 * Prüft, ob der Service eine Excel-Datei korrekt verarbeitet und
 * ein positives Import-Ergebnis zurückgibt, wenn alle Abhängigkeiten erwartungsgemäß reagieren.
 * Testfall deckt typischen Happy Path ab (kein Fehlerfall).
 */
class ExcelImportServiceTest {

    /**
     * Prüft, ob importExcel() bei gültigen Daten ein erfolgreiches Ergebnis zurückliefert.
     * - Mocks für alle Repository- und Utility-Abhängigkeiten.
     * - Dummy-Projektstruktur (Region, Address, Project) wird simuliert.
     * - ExcelUtil simuliert das Parsen einer Excel-Datei und gibt das Dummy-Projekt zurück.
     * - Alle Repository-Aufrufe werden entsprechend gemockt.
     * - Erwartet wird: Erfolg, 1 verarbeiteter Datensatz, keine Fehler.
     */
    @Test
    void importExcel_returnsSuccess_whenValidData() throws Exception {
        // Mocks für Repositorys und Util
        RegionRepository regionRepository = 	Mockito.mock(RegionRepository.class);
        AddressRepository addressRepository = 	Mockito.mock(AddressRepository.class);
        ProjectRepository projectRepository = 	Mockito.mock(ProjectRepository.class);
        ExcelUtil excelUtil = 					Mockito.mock(ExcelUtil.class);

        // Dummy-Entities für Testdaten
        Region region = 	new Region();
        region.setName		("Wien");

        Address address =	new Address();
        address.setRegion	(region);

        Project project = 	new Project();
        project.setAddress	(address);

        // ExcelUtil.parse gibt eine Liste mit dem Dummy-Projekt zurück
        Mockito.when(excelUtil.parse(any())).thenReturn(Collections.singletonList(project));
        // Repositories simulieren: noch keine Regionen vorhanden, speichern klappt
        Mockito.when(regionRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(regionRepository.save(any(Region.class))).thenReturn(region);
        Mockito.when(addressRepository.save(any(Address.class))).thenReturn(address);
        Mockito.when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Service mit gemockten Abhängigkeiten instanziieren
        ExcelImportService service = new ExcelImportService(
                regionRepository, addressRepository, projectRepository, excelUtil);

        // Dummy-Datei für den Upload simulieren
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.xlsx", 
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                new byte[]{1,2,3}
        );

        // Methode ausführen
        ImportResultDto result = service.importExcel(file);

        // Erwartung: Erfolg, 1 Datensatz verarbeitet, keine Fehler
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getRecordsProcessed()).isEqualTo(1);
        assertThat(result.getErrors()).isEmpty();
    }
}

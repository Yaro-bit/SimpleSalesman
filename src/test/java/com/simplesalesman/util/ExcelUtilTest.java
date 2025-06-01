package com.simplesalesman.util;

import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Project;
import com.simplesalesman.entity.Region;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcelUtilTest {

    @Test
    @DisplayName("parse() liest Projekte aus test_import.xlsx korrekt ein")
    void parse_shouldParseProjectsFromExcel() throws Exception {
        // Arrange: Testdatei laden
        InputStream is = getClass().getClassLoader().getResourceAsStream("test_import.xlsx");
        assertNotNull(is, "Testdatei 'test_import.xlsx' nicht gefunden!");

        ExcelUtil excelUtil = new ExcelUtil();

        // Act
        List<Project> projects = excelUtil.parse(is);

        // Assert
        assertNotNull(projects);
        assertFalse(projects.isEmpty());

        Project p = projects.get(0);

        assertNotNull(p.getAddress());
        Address a = p.getAddress();
        assertNotNull(a.getRegion());
        Region r = a.getRegion();

        assertEquals("Adlwang 92018-011", r.getName());
        assertEquals("Emsenhuber Straße 1, 4541, Adlwang", a.getAddressText());
        assertEquals(LocalDate.of(2019, 9, 30), p.getPlannedConstructionEnd());
        assertTrue(p.isConstructionCompleted()); // "True" als Boolean
        assertEquals("BBOÖ Breitband Oberösterreich GmbH", p.getOperator());
        assertEquals("100 In Betrieb", p.getStatus());
        assertEquals(10, p.getNumberOfHomes()); // In deiner Datei steht 10!
        assertFalse(p.isContractPresent());     // In deiner Datei steht vermutlich "0" oder "Nein"
        assertEquals("B", p.getCommissionCategory());
        assertEquals("49004", p.getKgNumber());
        assertEquals("Held & Francke", p.getConstructionCompany());
        assertEquals(LocalDate.of(2024, 3, 14), p.getSalesStart());
        assertNull(p.getSalesEnd()); // Feld ist leer/NaN
        assertEquals(new BigDecimal("291.67"), p.getProductPrice());
        assertFalse(p.isOutdoorFeePresent()); // "Nein"
    }
}

// 3. ExcelExportUtil.java - Refactored for Projects and Address-linked Notes
package com.simplesalesman.exam.export;

import com.simplesalesman.entity.Project;
import com.simplesalesman.entity.Note;
import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Region;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Utility class for creating Excel (XLSX) files from Project and Note entities.
 *
 * Creates multi-sheet Excel files with separate sheets for projects and notes.
 * Notes are linked to addresses, which are linked to projects.
 * Maintains backward compatibility with single-sheet project exports.
 *
 * @author Yaroslav Volokhodko
 * @version exam
 * @since 0.0.6
 */
@Component
public class ExcelExportUtil {

    // Project sheet headers (unchanged)
    private static final String[] PROJECT_HEADERS = {
        "Nr", "Region", "Bauende geplant", "Bauende erfolgt", "Adresse",
        "Betreiber", "Status", "Anzahl Wohnungen", "Vertrag vorhanden",
        "Provisionsart", "KG-Nummer", "Bauunternehmen", "Verkaufsstart",
        "Verkaufsende", "Produktpreis", "Außenanteil vorhanden"
    };

    // Note sheet headers (updated for address-linked notes)
    private static final String[] NOTE_HEADERS = {
        "Nr", "Adress ID", "Adresse Text", "Region", "Notiz Text",
        "Erstellt am", "Erstellt von", "Projekt ID", "Projekt Betreiber"
    };

    /**
     * Creates an Excel file with both projects and notes in separate sheets.
     */
    public byte[] createExcelWithProjectsAndNotes(List<Project> projects, List<Note> notes) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle dateTimeStyle = createDateTimeStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            
            // Create Projects sheet
            Sheet projectsSheet = workbook.createSheet("Projects");
            createProjectsSheet(projectsSheet, projects, headerStyle, dateStyle, currencyStyle);
            
            // Create Notes sheet
            Sheet notesSheet = workbook.createSheet("Notes");
            createNotesSheet(notesSheet, notes, projects, headerStyle, dateTimeStyle);
            
            // Convert to byte array
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    /**
     * Creates an Excel file with only projects (legacy compatibility).
     */
    public byte[] createExcelWithProjectsOnly(List<Project> projects) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Projects");
            
            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            // Create header row
            createProjectHeaderRow(sheet, headerStyle);
            
            // Create data rows
            createProjectDataRows(sheet, projects, workbook);
            
            // Auto-size columns
            autoSizeColumns(sheet, PROJECT_HEADERS.length);
            
            // Convert to byte array
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    /**
     * Creates an Excel file with only notes.
     */
    public byte[] createExcelWithNotesOnly(List<Note> notes) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Notes");
            
            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateTimeStyle = createDateTimeStyle(workbook);
            
            // Create header row
            createNoteHeaderRow(sheet, headerStyle);
            
            // Create data rows
            createNoteDataRows(sheet, notes, List.of(), dateTimeStyle);
            
            // Auto-size columns
            autoSizeColumns(sheet, NOTE_HEADERS.length);
            
            // Convert to byte array
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    /**
     * Legacy method for creating Excel with projects only.
     * @deprecated Use createExcelWithProjectsOnly() instead
     */
    @Deprecated
    public byte[] createExcel(List<Project> projects) throws Exception {
        return createExcelWithProjectsOnly(projects);
    }

    /**
     * Creates the projects sheet.
     */
    private void createProjectsSheet(Sheet sheet, List<Project> projects, 
                                   CellStyle headerStyle, CellStyle dateStyle, CellStyle currencyStyle) {
        // Create header row
        createProjectHeaderRow(sheet, headerStyle);
        
        // Create data rows
        createProjectDataRows(sheet, projects, sheet.getWorkbook());
        
        // Auto-size columns
        autoSizeColumns(sheet, PROJECT_HEADERS.length);
    }

    /**
     * Creates the notes sheet.
     */
    private void createNotesSheet(Sheet sheet, List<Note> notes, List<Project> projects,
                                CellStyle headerStyle, CellStyle dateTimeStyle) {
        // Create header row
        createNoteHeaderRow(sheet, headerStyle);
        
        // Create data rows
        createNoteDataRows(sheet, notes, projects, dateTimeStyle);
        
        // Auto-size columns
        autoSizeColumns(sheet, NOTE_HEADERS.length);
    }

    /**
     * Creates a styled header row for projects.
     */
    private void createProjectHeaderRow(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        
        for (int i = 0; i < PROJECT_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(PROJECT_HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * Creates a styled header row for notes.
     */
    private void createNoteHeaderRow(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        
        for (int i = 0; i < NOTE_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(NOTE_HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * Creates data rows from project list.
     */
    private void createProjectDataRows(Sheet sheet, List<Project> projects, Workbook workbook) {
        CellStyle dateStyle = createDateStyle(workbook);
        CellStyle currencyStyle = createCurrencyStyle(workbook);
        
        int rowNum = 1;
        for (Project project : projects) {
            Row row = sheet.createRow(rowNum++);
            populateProjectRow(row, project, rowNum - 1, dateStyle, currencyStyle);
        }
    }

    /**
     * Creates data rows from note list.
     */
    private void createNoteDataRows(Sheet sheet, List<Note> notes, List<Project> projects, 
                                  CellStyle dateTimeStyle) {
        int rowNum = 1;
        for (Note note : notes) {
            Row row = sheet.createRow(rowNum++);
            populateNoteRow(row, note, projects, rowNum - 1, dateTimeStyle);
        }
    }

    /**
     * Populates a single row with project data.
     */
    private void populateProjectRow(Row row, Project project, int rowNumber, 
                                   CellStyle dateStyle, CellStyle currencyStyle) {
        Address address = project.getAddress();
        Region region = address != null ? address.getRegion() : null;
        
        // Column 0: Row Number
        createCell(row, 0, rowNumber);
        
        // Column 1: Region Name
        createCell(row, 1, region != null ? region.getName() : "");
        
        // Column 2: Construction End Date
        createDateCell(row, 2, project.getPlannedConstructionEnd(), dateStyle);
        
        // Column 3: Construction Completed
        createCell(row, 3, formatBoolean(project.isConstructionCompleted()));
        
        // Column 4: Address Text
        createCell(row, 4, address != null ? address.getAddressText() : "");
        
        // Column 5: Operator
        createCell(row, 5, project.getOperator());
        
        // Column 6: Status
        createCell(row, 6, project.getStatus());
        
        // Column 7: Number of Homes
        createCell(row, 7, project.getNumberOfHomes());
        
        // Column 8: Contract Present
        createCell(row, 8, formatBoolean(project.isContractPresent()));
        
        // Column 9: Commission Category
        createCell(row, 9, project.getCommissionCategory());
        
        // Column 10: KG Number
        createCell(row, 10, project.getKgNumber());
        
        // Column 11: Construction Company
        createCell(row, 11, project.getConstructionCompany());
        
        // Column 12: Sales Start
        createDateCell(row, 12, project.getSalesStart(), dateStyle);
        
        // Column 13: Sales End
        createDateCell(row, 13, project.getSalesEnd(), dateStyle);
        
        // Column 14: Product Price
        createCurrencyCell(row, 14, project.getProductPrice(), currencyStyle);
        
        // Column 15: Outdoor Fee Present
        createCell(row, 15, formatBoolean(project.isOutdoorFeePresent()));
    }

    /**
     * Populates a single row with note data (address-linked).
     */
    private void populateNoteRow(Row row, Note note, List<Project> projects, int rowNumber, 
                                CellStyle dateTimeStyle) {
        Address noteAddress = note.getAddress();
        Region region = noteAddress != null ? noteAddress.getRegion() : null;
        
        // Find the associated project for this address
        Project associatedProject = projects.stream()
            .filter(p -> p.getAddress() != null && 
                        noteAddress != null && 
                        p.getAddress().getId().equals(noteAddress.getId()))
            .findFirst()
            .orElse(null);
        
        // Column 0: Row Number
        createCell(row, 0, rowNumber);
        
        // Column 1: Address ID
        createCell(row, 1, noteAddress != null ? noteAddress.getId().toString() : "");
        
        // Column 2: Address Text
        createCell(row, 2, noteAddress != null ? noteAddress.getAddressText() : "");
        
        // Column 3: Region
        createCell(row, 3, region != null ? region.getName() : "");
        
        // Column 4: Note Text
        createCell(row, 4, note.getText());
        
        // Column 5: Created At
        createDateTimeCell(row, 5, note.getCreatedAt(), dateTimeStyle);
        
        // Column 6: Created By
        createCell(row, 6, note.getCreatedBy());
        
        // Column 7: Project ID (if associated project exists)
        createCell(row, 7, associatedProject != null ? associatedProject.getId().toString() : "");
        
        // Column 8: Project Operator (if associated project exists)
        createCell(row, 8, associatedProject != null ? associatedProject.getOperator() : "");
    }

    /**
     * Creates a cell with string value.
     */
    private void createCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value != null ? value : "");
    }

    /**
     * Creates a cell with numeric value.
     */
    private void createCell(Row row, int column, int value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    /**
     * Creates a cell with date value.
     */
    private void createDateCell(Row row, int column, LocalDate date, CellStyle dateStyle) {
        Cell cell = row.createCell(column);
        if (date != null) {
            cell.setCellValue(date);
            cell.setCellStyle(dateStyle);
        }
    }

    /**
     * Creates a cell with datetime value.
     */
    private void createDateTimeCell(Row row, int column, LocalDateTime dateTime, CellStyle dateTimeStyle) {
        Cell cell = row.createCell(column);
        if (dateTime != null) {
            cell.setCellValue(dateTime);
            cell.setCellStyle(dateTimeStyle);
        }
    }

    /**
     * Creates a cell with currency value.
     */
    private void createCurrencyCell(Row row, int column, java.math.BigDecimal value, CellStyle currencyStyle) {
        Cell cell = row.createCell(column);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
            cell.setCellStyle(currencyStyle);
        }
    }

    /**
     * Creates header cell style.
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        
        return style;
    }

    /**
     * Creates date cell style.
     */
    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd.mm.yyyy"));
        return style;
    }

    /**
     * Creates datetime cell style.
     */
    private CellStyle createDateTimeStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd.mm.yyyy hh:mm:ss"));
        return style;
    }

    /**
     * Creates currency cell style.
     */
    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00 €"));
        return style;
    }

    /**
     * Auto-sizes all columns.
     */
    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Formats boolean values for Excel.
     */
    private String formatBoolean(boolean value) {
        return value ? "Ja" : "Nein";
    }
}
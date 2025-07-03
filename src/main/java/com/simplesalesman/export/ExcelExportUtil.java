// 3. ExcelExportUtil.java
package com.simplesalesman.export;

import com.simplesalesman.entity.Project;
import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Region;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

/**
 * Utility class for creating Excel (XLSX) files from Project entities.
 *
 * Excel Format (matching import structure):
 * - [0] Row Number
 * - [1] Region Name
 * - [2] Construction End Date
 * - [3] Construction Completed
 * - [4] Address Text
 * - [5-15] Project fields
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.6
 */
@Component
public class ExcelExportUtil {

    private static final String[] HEADERS = {
        "Nr", "Region", "Bauende geplant", "Bauende erfolgt", "Adresse",
        "Betreiber", "Status", "Anzahl Wohnungen", "Vertrag vorhanden",
        "Provisionsart", "KG-Nummer", "Bauunternehmen", "Verkaufsstart",
        "Verkaufsende", "Produktpreis", "Außenanteil vorhanden"
    };

    /**
     * Creates an Excel file from a list of Project entities.
     */
    public byte[] createExcel(List<Project> projects) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Projects");
            
            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            // Create header row
            createHeaderRow(sheet, headerStyle);
            
            // Create data rows
            createDataRows(sheet, projects, workbook);
            
            // Auto-size columns
            autoSizeColumns(sheet);
            
            // Convert to byte array
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    /**
     * Creates a styled header row.
     */
    private void createHeaderRow(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * Creates data rows from project list.
     */
    private void createDataRows(Sheet sheet, List<Project> projects, Workbook workbook) {
        CellStyle dateStyle = createDateStyle(workbook);
        CellStyle currencyStyle = createCurrencyStyle(workbook);
        
        int rowNum = 1;
        for (Project project : projects) {
            Row row = sheet.createRow(rowNum++);
            populateProjectRow(row, project, rowNum - 1, dateStyle, currencyStyle);
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
    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < HEADERS.length; i++) {
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
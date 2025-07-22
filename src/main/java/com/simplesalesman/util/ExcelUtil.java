package com.simplesalesman.util;

import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Project;
import com.simplesalesman.entity.Region;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class for parsing Excel (XLSX) files into {@link Project} entities.
 *
 * This component is responsible for reading and transforming Excel rows into
 * valid domain objects including {@link Region}, {@link Address}, and {@link Project}.
 * It handles various cell formats (dates, booleans, decimals) and provides robust
 * fallback behavior for unexpected or malformed data.
 *
 * Usage:
 * - Used exclusively by {@link com.simplesalesman.service.ExcelImportService}
 * - Expects a specific column structure in the first worksheet
 *
 * Excel Format Assumptions (0-based columns):
 * - [1] Region Name (String)
 * - [2] Construction End Date (Date)
 * - [3] Construction Completed (Boolean)
 * - [4] Address Text (String)
 * - [5–15] Project fields
 *
 * Error Handling:
 * - Faulty rows are skipped individually with logging
 * - Fields with invalid formats are defaulted (e.g., 0, false, null)
 * - Too many errors trigger an exception to prevent bad imports
 *
 * @author SimpleSalesman Team
 * @version 0.1.0
 * @since 0.0.5
 */
@Component
public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    private static final int MAX_ERRORS = 1000; // Stop processing if too many errors

    /**
     * Parses the given Excel input stream and converts rows to a list of {@link Project} objects.
     *
     * @param inputStream input stream of uploaded Excel file (XLSX)
     * @return list of Project entities parsed from the file
     * @throws Exception on structural or file format issues
     */
    public List<Project> parse(InputStream inputStream) throws Exception {
        List<Project> projects = new ArrayList<>();
        int rowNum = 0;
        int successCount = 0;
        int errorCount = 0;

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            if (sheet == null) {
                throw new IllegalArgumentException("Excel file must contain at least one worksheet");
            }
            
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            } else {
                throw new IllegalArgumentException("Excel file appears to be empty");
            }

            while (rows.hasNext()) {
                Row row = rows.next();
                rowNum = row.getRowNum();

                try {
                    Project project = parseRow(row);
                    if (project != null) {
                        projects.add(project);
                        successCount++;
                    }
                } catch (Exception e) {
                    errorCount++;
                    logger.warn("Error parsing row {}: {}", rowNum, e.getMessage());
                    
                    if (errorCount > MAX_ERRORS) {
                        throw new RuntimeException(
                            String.format("Too many errors in Excel file (>%d). Last error at row %d: %s", 
                                MAX_ERRORS, rowNum, e.getMessage())
                        );
                    }
                }
                
                // Log progress every 1000 rows
                if ((rowNum % 1000) == 0 && rowNum > 0) {
                    logger.info("Progress: Processed {} rows, {} successful, {} errors", 
                        rowNum, successCount, errorCount);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to parse Excel file at row {}: {}", rowNum, e.getMessage());
            throw e;
        }
        
        logger.info("Excel parsing complete. Total rows: {}, Success: {}, Errors: {}", 
            rowNum, successCount, errorCount);
        
        if (projects.isEmpty() && errorCount > 0) {
            throw new IllegalArgumentException("No valid data could be parsed from the Excel file");
        }
        
        return projects;
    }

    private Project parseRow(Row row) throws IllegalArgumentException {
        Project project = new Project();
        Address address = new Address();
        Region region = new Region();

        // Validate required fields
        String regionName = getCellValue(row.getCell(1));
        String addressText = getCellValue(row.getCell(4));
        
        if (regionName.isEmpty()) {
            throw new IllegalArgumentException("Region name is required (column B)");
        }
        if (addressText.isEmpty()) {
            throw new IllegalArgumentException("Address text is required (column E)");
        }

        // Set basic region and address
        region.setName(regionName);
        address.setAddressText(addressText);
        address.setRegion(region);
        project.setAddress(address);

        // Set project fields with safe parsing
        project.setPlannedConstructionEnd(parseDate(row.getCell(2)));
        project.setConstructionCompleted(parseBoolean(getCellValue(row.getCell(3))));
        project.setOperator(getCellValue(row.getCell(5)));
        project.setStatus(getCellValue(row.getCell(6)));
        project.setNumberOfHomes(parseInt(getCellValue(row.getCell(7))));
        project.setContractPresent(parseContractPresent(getCellValue(row.getCell(8))));
        project.setCommissionCategory(getCellValue(row.getCell(9)));
        project.setKgNumber(getCellValue(row.getCell(10)));
        project.setConstructionCompany(getCellValue(row.getCell(11)));
        project.setSalesStart(parseDate(row.getCell(12)));
        project.setSalesEnd(parseDate(row.getCell(13)));
        project.setProductPrice(parseDecimal(row.getCell(14)));
        project.setOutdoorFeePresent(parseBoolean(getCellValue(row.getCell(15))));

        return project;
    }

    /**
     * Converts any cell type into a trimmed String representation.
     *
     * @param cell Excel cell
     * @return stringified value
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        
        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                    } else {
                        // Handle integers without decimal point
                        double numValue = cell.getNumericCellValue();
                        if (numValue == Math.floor(numValue)) {
                            return String.valueOf((long) numValue);
                        }
                        return String.valueOf(numValue).trim();
                    }
                case BOOLEAN:
                    return Boolean.toString(cell.getBooleanCellValue());
                case FORMULA:
                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    return getCellValue(evaluator.evaluateInCell(cell));
                default:
                    return "";
            }
        } catch (Exception e) {
            logger.debug("Error reading cell value at row {} column {}: {}", 
                cell.getRowIndex(), cell.getColumnIndex(), e.getMessage());
            return "";
        }
    }

    /**
     * Parses a {@link LocalDate} from a cell if it's formatted as a date or contains ISO date text.
     *
     * @param cell the Excel cell
     * @return parsed LocalDate or null
     */
    private LocalDate parseDate(Cell cell) {
        try {
            if (cell == null) return null;
            
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                if (!value.isEmpty()) {
                    return LocalDate.parse(value);
                }
            }
        } catch (Exception e) {
            logger.debug("Invalid date value in cell: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Parses a numeric string to int, removing any non-digit characters.
     *
     * @param s input string
     * @return integer or 0
     */
    private int parseInt(String s) {
        if (s == null || s.isEmpty()) return 0;
        
        try {
            // Handle decimal values by truncating
            if (s.contains(".")) {
                return (int) Double.parseDouble(s);
            }
            String cleaned = s.replaceAll("[^\\d-]", "");
            return cleaned.isEmpty() ? 0 : Integer.parseInt(cleaned);
        } catch (Exception e) {
            logger.debug("Failed to parse integer from '{}': {}", s, e.getMessage());
            return 0;
        }
    }

    /**
     * Parses a boolean value from text like "ja", "1", "true", "wahr".
     *
     * @param value the input string
     * @return true or false
     */
    private boolean parseBoolean(String value) {
        if (value == null) return false;
        String v = value.trim().toLowerCase();
        return v.equals("true") || v.equals("1") || v.equals("ja") || v.equals("wahr") || v.equals("yes");
    }

    /**
     * Parses contract presence from values like "1", "ja", "true".
     *
     * @param value cell content
     * @return true if contract is present
     */
    private boolean parseContractPresent(String value) {
        return parseBoolean(value);
    }

    /**
     * Parses a decimal value from a cell, accepting both numeric and comma-separated strings.
     *
     * @param cell the Excel cell
     * @return parsed BigDecimal or 0
     */
    private BigDecimal parseDecimal(Cell cell) {
        try {
            if (cell != null) {
                if (cell.getCellType() == CellType.NUMERIC) {
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                } else if (cell.getCellType() == CellType.STRING) {
                    String s = cell.getStringCellValue()
                        .replace(",", ".")
                        .replaceAll("[^\\d.-]", "");
                    if (!s.isEmpty()) {
                        return new BigDecimal(s);
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Error parsing decimal value: {}", e.getMessage());
        }
        return BigDecimal.ZERO;
    }
}
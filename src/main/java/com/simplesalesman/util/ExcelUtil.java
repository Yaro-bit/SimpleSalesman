package com.simplesalesman.util;

import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Project;
import com.simplesalesman.entity.Region;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 * - Faulty rows are skipped individually with logging to stderr
 * - Fields with invalid formats are defaulted (e.g., 0, false, null)
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.5
 */
@Component
public class ExcelUtil {

    /**
     * Parses the given Excel input stream and converts rows to a list of {@link Project} objects.
     *
     * @param inputStream input stream of uploaded Excel file (XLSX)
     * @return list of Project entities parsed from the file
     * @throws Exception on structural or file format issues
     */
    public List<Project> parse(InputStream inputStream) throws Exception {
        List<Project> projects = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // Skip header row

            while (rows.hasNext()) {
                Row row = rows.next();

                try {
                    Project project = new Project();
                    Address address = new Address();
                    Region region = new Region();

                    // Set basic region and address
                    region.setName(getCellValue(row.getCell(1)));
                    address.setAddressText(getCellValue(row.getCell(4)));
                    address.setRegion(region);
                    project.setAddress(address);

                    // Set project fields
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

                    projects.add(project);
                } catch (Exception e) {
                    System.err.println("⚠️ Fehler beim Verarbeiten von Zeile " + row.getRowNum() + ": " + e.getMessage());
                }
            }
        }

        return projects;
    }

    /**
     * Converts any cell type into a trimmed String representation.
     *
     * @param cell Excel cell
     * @return stringified value
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue()).trim();
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                return getCellValue(evaluator.evaluateInCell(cell));
            default:
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
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                if (!value.isEmpty()) {
                    return LocalDate.parse(value);
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Ungültiges Datum: " + e.getMessage());
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
        try {
            String cleaned = s.replaceAll("[^\\d]", "");
            return cleaned.isEmpty() ? 0 : Integer.parseInt(cleaned);
        } catch (Exception e) {
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
        String v = value.trim().toLowerCase();
        return v.equals("true") || v.equals("1") || v.equals("ja") || v.equals("wahr");
    }

    /**
     * Parses contract presence from values like "1", "ja", "true".
     *
     * @param value cell content
     * @return true if contract is present
     */
    private boolean parseContractPresent(String value) {
        String v = value.trim().toLowerCase();
        return v.equals("1") || v.equals("ja") || v.equals("true") || v.equals("wahr");
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
                    String s = cell.getStringCellValue().replace(",", ".").replaceAll("[^\\d.]", "");
                    if (!s.isEmpty()) {
                        return new BigDecimal(s);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Fehler bei Dezimalwert: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
}

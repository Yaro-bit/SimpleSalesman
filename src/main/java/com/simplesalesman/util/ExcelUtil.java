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

@Component
public class ExcelUtil {

    public List<Project> parse(InputStream inputStream) throws Exception {
        List<Project> projects = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // Header überspringen

            while (rows.hasNext()) {
                Row row = rows.next();

                try {
                    Project project = new Project();
                    Address address = new Address();
                    Region region = new Region();

                    region.setName(getCellValue(row.getCell(1)));
                    address.setAddressText(getCellValue(row.getCell(4)));
                    address.setRegion(region);
                    project.setAddress(address);

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

    // Gibt den Zellwert IMMER als String zurück, unabhängig vom Typ
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
                // Formelzellen evaluieren
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                return getCellValue(evaluator.evaluateInCell(cell));
            case BLANK:
            case _NONE:
            case ERROR:
            default:
                return "";
        }
    }

    // Erkennt sowohl Excel-Datumszellen als auch ISO-Date-Strings
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

    private int parseInt(String s) {
        try {
            String cleaned = s.replaceAll("[^\\d]", "");
            return cleaned.isEmpty() ? 0 : Integer.parseInt(cleaned);
        } catch (Exception e) {
            return 0;
        }
    }

    // Erkennt "ja", "1", "true", "wahr" als true; "nein", "0", "false" als false
    private boolean parseBoolean(String value) {
        String v = value.trim().toLowerCase();
        return v.equals("true") || v.equals("1") || v.equals("ja") || v.equals("wahr");
    }

    // Erkennt "1", "ja", "true" als true für Vertrag/Status-Felder
    private boolean parseContractPresent(String value) {
        String v = value.trim().toLowerCase();
        return v.equals("1") || v.equals("ja") || v.equals("true") || v.equals("wahr");
    }

    // Erkennt sowohl numerische als auch String-Werte, konvertiert ,/./leer
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

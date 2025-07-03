// 1. ExportController.java
package com.simplesalesman.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * REST Controller for handling file export operations in the SimpleSalesman application.
 *
 * This controller provides endpoints for exporting data from the application database
 * to external files, particularly Excel files. It handles data retrieval, formatting,
 * and file generation with proper HTTP headers for download.
 *
 * @author SimpleSalesman Team
 * @version 0.0.9
 * @since 0.0.9
 */
@RestController
@RequestMapping("/api/v1/export")
@CrossOrigin(origins = "*")
public class ExportController {

    private static final Logger logger = LoggerFactory.getLogger(ExportController.class);
    private static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    
    private final ExcelExportService excelExportService;

    public ExportController(ExcelExportService excelExportService) {
        this.excelExportService = excelExportService;
        logger.info("ExportController initialized");
    }

    /**
     * Exports all projects to an Excel file.
     */
    @GetMapping("/excel")
    public ResponseEntity<ByteArrayResource> exportProjectsToExcel() {
        logger.info("GET request received for Excel export of all projects");
        
        try {
            long startTime = System.currentTimeMillis();
            byte[] excelData = excelExportService.exportProjectsToExcel();
            long processingTime = System.currentTimeMillis() - startTime;
            
            String filename = generateFilename("SimpleSalesman_Projects");
            
            logger.info("Excel export completed successfully. File size: {} bytes, Processing time: {} ms", 
                       excelData.length, processingTime);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE))
                    .contentLength(excelData.length)
                    .body(new ByteArrayResource(excelData));
                    
        } catch (Exception e) {
            logger.error("Error during Excel export", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exports projects by region to an Excel file.
     */
    @GetMapping("/excel/region/{regionId}")
    public ResponseEntity<ByteArrayResource> exportProjectsByRegion(@PathVariable Long regionId) {
        logger.info("GET request received for Excel export of projects in region: {}", regionId);
        
        try {
            long startTime = System.currentTimeMillis();
            byte[] excelData = excelExportService.exportProjectsByRegion(regionId);
            long processingTime = System.currentTimeMillis() - startTime;
            
            String filename = generateFilename("SimpleSalesman_Projects_Region_" + regionId);
            
            logger.info("Excel export by region completed successfully. Region: {}, File size: {} bytes, Processing time: {} ms", 
                       regionId, excelData.length, processingTime);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE))
                    .contentLength(excelData.length)
                    .body(new ByteArrayResource(excelData));
                    
        } catch (Exception e) {
            logger.error("Error during Excel export for region: {}", regionId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Generates a timestamped filename for the export.
     */
    private String generateFilename(String baseName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        return baseName + "_" + timestamp + ".xlsx";
    }
}
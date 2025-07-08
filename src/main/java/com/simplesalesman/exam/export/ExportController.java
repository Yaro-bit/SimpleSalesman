// 1. ExportController.java - Refactored for Projects and Notes
package com.simplesalesman.exam.export;

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
 * Updated to support exporting both projects and notes data.
 *
 * @author Yaroslav Volokhodko
 * @version exam
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
        logger.info("ExportController initialized with projects and notes support");
    }

    /**
     * Exports all projects and their associated notes to an Excel file.
     */
    @GetMapping("/excel")
    public ResponseEntity<ByteArrayResource> exportProjectsAndNotesToExcel() {
        logger.info("GET request received for Excel export of all projects and notes");
        
        try {
            long startTime = System.currentTimeMillis();
            byte[] excelData = excelExportService.exportProjectsAndNotesToExcel();
            long processingTime = System.currentTimeMillis() - startTime;
            
            String filename = generateFilename("SimpleSalesman_Projects_And_Notes");
            
            logger.info("Excel export completed successfully. File size: {} bytes, Processing time: {} ms", 
                       excelData.length, processingTime);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE))
                    .contentLength(excelData.length)
                    .body(new ByteArrayResource(excelData));
                    
        } catch (Exception e) {
            logger.error("Error during Excel export of projects and notes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exports projects and notes by region to an Excel file.
     */
    @GetMapping("/excel/region/{regionId}")
    public ResponseEntity<ByteArrayResource> exportProjectsAndNotesByRegion(@PathVariable Long regionId) {
        logger.info("GET request received for Excel export of projects and notes in region: {}", regionId);
        
        try {
            long startTime = System.currentTimeMillis();
            byte[] excelData = excelExportService.exportProjectsAndNotesByRegion(regionId);
            long processingTime = System.currentTimeMillis() - startTime;
            
            String filename = generateFilename("SimpleSalesman_Projects_Notes_Region_" + regionId);
            
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
     * Exports only projects to an Excel file (legacy endpoint).
     */
    @GetMapping("/excel/projects-only")
    public ResponseEntity<ByteArrayResource> exportProjectsOnlyToExcel() {
        logger.info("GET request received for Excel export of projects only (legacy)");
        
        try {
            long startTime = System.currentTimeMillis();
            byte[] excelData = excelExportService.exportProjectsOnlyToExcel();
            long processingTime = System.currentTimeMillis() - startTime;
            
            String filename = generateFilename("SimpleSalesman_Projects_Only");
            
            logger.info("Excel export (projects only) completed successfully. File size: {} bytes, Processing time: {} ms", 
                       excelData.length, processingTime);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE))
                    .contentLength(excelData.length)
                    .body(new ByteArrayResource(excelData));
                    
        } catch (Exception e) {
            logger.error("Error during Excel export (projects only)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exports only notes to an Excel file.
     */
    @GetMapping("/excel/notes-only")
    public ResponseEntity<ByteArrayResource> exportNotesOnlyToExcel() {
        logger.info("GET request received for Excel export of notes only");
        
        try {
            long startTime = System.currentTimeMillis();
            byte[] excelData = excelExportService.exportNotesOnlyToExcel();
            long processingTime = System.currentTimeMillis() - startTime;
            
            String filename = generateFilename("SimpleSalesman_Notes_Only");
            
            logger.info("Excel export (notes only) completed successfully. File size: {} bytes, Processing time: {} ms", 
                       excelData.length, processingTime);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(EXCEL_CONTENT_TYPE))
                    .contentLength(excelData.length)
                    .body(new ByteArrayResource(excelData));
                    
        } catch (Exception e) {
            logger.error("Error during Excel export (notes only)", e);
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
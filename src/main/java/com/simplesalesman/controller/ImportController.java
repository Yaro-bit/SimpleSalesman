package com.simplesalesman.controller;

import com.simplesalesman.dto.ImportResultDto;
import com.simplesalesman.service.ExcelImportService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller for handling file import operations in the SimpleSalesman
 * application.
 *
 * This controller provides endpoints for importing data from external files,
 * particularly Excel files, into the application database. It handles file
 * upload, validation, processing, and provides detailed feedback about the
 * import operation results.
 *
 * Key features: - Excel file import with comprehensive validation - Detailed
 * import result reporting (success/failure counts) - File size and format
 * validation - Comprehensive error handling and logging - Progress tracking and
 * monitoring capabilities - Cross-origin support for web frontend integration
 *
 * API Endpoints: - POST /api/v1/import - Import data from uploaded Excel file
 *
 * Supported file formats: - Excel (.xlsx, .xls) - CSV files (future
 * enhancement)
 *
 * Security considerations: - File size limits to prevent DoS attacks - File
 * type validation to prevent malicious uploads - Content scanning for
 * potentially harmful data
 *
 * @author SimpleSalesman Team
 * @version 0.1.0
 * @since 0.0.4
 */
@RestController
@RequestMapping("/api/v1/import")
@CrossOrigin(origins = "*")
public class ImportController {

	private static final Logger logger = LoggerFactory.getLogger(ImportController.class);
	private static final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB limit - updated to match frontend expectations
	private static final String[] ALLOWED_CONTENT_TYPES = {
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xlsx
			"application/vnd.ms-excel", // .xls
			"application/octet-stream" // Some browsers send this for Excel files
	};

	private final ExcelImportService excelImportService;

	/**
	 * Constructor for ImportController.
	 *
	 * @param excelImportService The service layer component for Excel import
	 *                           operations
	 */
	public ImportController(ExcelImportService excelImportService) {
		this.excelImportService = excelImportService;
		logger.info("ImportController initialized with max file size: {} MB", MAX_FILE_SIZE / (1024 * 1024));
	}

	/**
	 * Imports data from an uploaded Excel file into the application database.
	 *
	 * This endpoint accepts Excel files (.xlsx, .xls) and processes them to import
	 * data into the system. It performs comprehensive validation of the file
	 * format, size, and content before processing. The operation provides detailed
	 * feedback about successful imports, failed records, and any errors
	 * encountered.
	 *
	 * File requirements: - Maximum file size: 100MB - Supported formats: Excel
	 * (.xlsx, .xls) - Must contain valid data structure as expected by the system
	 *
	 * @param file The Excel file to be imported (multipart form data)
	 * @return ResponseEntity containing ImportResultDto with detailed import
	 *         results Returns HTTP 200 (OK) with import results on successful
	 *         processing Returns HTTP 400 (Bad Request) if file validation fails
	 *         Returns HTTP 413 (Payload Too Large) if file exceeds size limit
	 *         Returns HTTP 415 (Unsupported Media Type) if file format is not
	 *         supported Returns HTTP 500 (Internal Server Error) if processing
	 *         fails
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ImportResultDto> importExcel(@RequestParam("file") MultipartFile file) {
		logger.info("POST request received for Excel import. File: '{}', Size: {} MB", 
			file.getOriginalFilename(), file.getSize() / (1024.0 * 1024.0));

		try {
			// Validate file is not empty
			if (file.isEmpty()) {
				logger.warn("Import failed: Empty file uploaded");
				ImportResultDto result = new ImportResultDto();
				result.setSuccess(false);
				result.setErrors(List.of("Empty file uploaded"));
				return ResponseEntity.badRequest().body(result);
			}

			// Validate file size
			if (file.getSize() > MAX_FILE_SIZE) {
				logger.warn("Import failed: File size ({} MB) exceeds maximum allowed size ({} MB)",
						file.getSize() / (1024.0 * 1024.0), MAX_FILE_SIZE / (1024.0 * 1024.0));
				ImportResultDto result = new ImportResultDto();
				result.setSuccess(false);
				result.setErrors(List.of("File size exceeds maximum allowed size of " + (MAX_FILE_SIZE / (1024 * 1024)) + " MB"));
				return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(result);
			}

			// Validate file type - more lenient checking
			String contentType = file.getContentType();
			String filename = file.getOriginalFilename();
			
			boolean isValidType = false;
			
			// Check by content type
			if (contentType != null) {
				for (String allowedType : ALLOWED_CONTENT_TYPES) {
					if (allowedType.equals(contentType)) {
						isValidType = true;
						break;
					}
				}
			}
			
			// Also check by file extension as fallback
			if (!isValidType && filename != null) {
				String lowerFilename = filename.toLowerCase();
				isValidType = lowerFilename.endsWith(".xlsx") || lowerFilename.endsWith(".xls");
			}

			if (!isValidType) {
				logger.warn("Import failed: Unsupported file type '{}' for file '{}'", contentType, filename);
				ImportResultDto result = new ImportResultDto();
				result.setSuccess(false);
				result.setErrors(List.of("Unsupported file type. Please upload an Excel file (.xlsx or .xls)"));
				return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(result);
			}

			logger.info("File validation passed. Starting import process for file: '{}' ({} MB)", 
				filename, file.getSize() / (1024.0 * 1024.0));

			// Process the import
			long startTime = System.currentTimeMillis();
			ImportResultDto result = excelImportService.importExcel(file);
			long processingTime = System.currentTimeMillis() - startTime;

			// Log import results
			if (result.isSuccess()) {
				logger.info("Import completed successfully. File: '{}', Records: {}, Processing time: {} seconds", 
					filename, result.getRecordsProcessed(), processingTime / 1000.0);
			} else {
				logger.warn("Import completed with errors. File: '{}', Records: {}, Errors: {}, Processing time: {} seconds", 
					filename, result.getRecordsProcessed(), result.getErrors().size(), processingTime / 1000.0);
			}

			// Log additional details if available in the result object
			logger.debug("Import result details: {}", result.toString());

			return ResponseEntity.ok(result);

		} catch (IllegalArgumentException e) {
			logger.error("Import failed due to invalid arguments: {}", e.getMessage());
			ImportResultDto result = new ImportResultDto();
			result.setSuccess(false);
			result.setErrors(List.of("Invalid file format: " + e.getMessage()));
			return ResponseEntity.badRequest().body(result);
		} catch (Exception e) {
			logger.error("Unexpected error during Excel import for file: '{}'", file.getOriginalFilename(), e);
			ImportResultDto result = new ImportResultDto();
			result.setSuccess(false);
			result.setErrors(List.of("Internal server error: " + e.getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
}
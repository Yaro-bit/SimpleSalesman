package com.simplesalesman.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Data Transfer Object representing the result of an Excel import operation.
 *
 * This object is returned by the import API to communicate:
 * - Whether the import was successful
 * - How many records were processed
 * - Any errors that occurred during the import
 *
 * Example response:
 * {
 *   "success": true,
 *   "recordsProcessed": 42,
 *   "errors": []
 * }
 *
 * Logging is included for traceability and debugging during the import process.
 *
 * Author: SimpleSalesman Team
 * @version 0.0.5
 * @since 0.0.3
 */
public class ImportResultDto {

    private static final Logger logger = LoggerFactory.getLogger(ImportResultDto.class);

    /**
     * Indicates if the import completed successfully.
     */
    private boolean success;

    /**
     * The number of records that were successfully processed.
     */
    private int recordsProcessed;

    /**
     * A list of error messages describing failed rows or validation issues.
     */
    private List<String> errors;

    /**
     * Default constructor.
     */
    public ImportResultDto() {
        logger.trace("New ImportResultDto instance created");
    }

    /**
     * Constructor with all fields.
     *
     * @param success           true if import was successful
     * @param recordsProcessed  number of successfully processed records
     * @param errors            list of error messages (if any)
     */
    public ImportResultDto(boolean success, int recordsProcessed, List<String> errors) {
        this.success = success;
        this.recordsProcessed = recordsProcessed;
        this.errors = errors;
        logger.debug("ImportResultDto created with success={}, recordsProcessed={}, errorCount={}",
                success, recordsProcessed, errors != null ? errors.size() : 0);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        logger.debug("ImportResultDto success flag set to {}", success);
        this.success = success;
    }

    public int getRecordsProcessed() {
        return recordsProcessed;
    }

    public void setRecordsProcessed(int recordsProcessed) {
        logger.debug("ImportResultDto recordsProcessed set to {}", recordsProcessed);
        this.recordsProcessed = recordsProcessed;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        logger.debug("ImportResultDto errors updated: {} error(s)", errors != null ? errors.size() : 0);
        this.errors = errors;
    }
}

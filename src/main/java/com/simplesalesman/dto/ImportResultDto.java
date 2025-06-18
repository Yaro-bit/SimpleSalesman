package com.simplesalesman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing the result of an Excel import
 * operation in the SimpleSalesman system.
 *
 * Used by the import controller to return: - Whether the import was successful
 * - How many records were processed - Any validation or processing errors
 *
 * Example JSON response: { "success": true, "recordsProcessed": 42, "errors":[]
 * }
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.4
 */
@Schema(description = "Result of an Excel import operation")
public class ImportResultDto {

	private static final Logger log = LoggerFactory.getLogger(ImportResultDto.class);

	@Schema(description = "Indicates if the import was successful", example = "true")
	private boolean success;

	@Schema(description = "Number of records successfully processed", example = "42")
	private int recordsProcessed;

	@Schema(description = "List of error messages", example = "[\"Invalid format in row 3\"]")
	private List<String> errors;

	/**
	 * Default constructor.
	 */
	public ImportResultDto() {
		log.trace("New ImportResultDto instance created");
	}

	/**
	 * Full constructor.
	 *
	 * @param success          whether the import was successful
	 * @param recordsProcessed number of successfully imported records
	 * @param errors           list of error messages
	 */
	public ImportResultDto(boolean success, int recordsProcessed, List<String> errors) {
		this.success = success;
		this.recordsProcessed = recordsProcessed;
		this.errors = errors != null ? errors : Collections.emptyList();
		log.debug("ImportResultDto created: success={}, recordsProcessed={}, errorCount={}", success, recordsProcessed,
				this.errors.size());
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		log.debug("ImportResultDto success set to {}", success);
		this.success = success;
	}

	public int getRecordsProcessed() {
		return recordsProcessed;
	}

	public void setRecordsProcessed(int recordsProcessed) {
		log.debug("ImportResultDto recordsProcessed set to {}", recordsProcessed);
		this.recordsProcessed = recordsProcessed;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		int size = errors != null ? errors.size() : 0;
		log.debug("ImportResultDto errors updated: {} error(s)", size);
		this.errors = errors != null ? errors : Collections.emptyList();
	}

	@Override
	public String toString() {
		return String.format("ImportResultDto{success=%s, recordsProcessed=%d, errorCount=%d}", success,
				recordsProcessed, errors != null ? errors.size() : 0);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ImportResultDto))
			return false;
		ImportResultDto that = (ImportResultDto) o;
		return success == that.success && recordsProcessed == that.recordsProcessed
				&& Objects.equals(errors, that.errors);
	}

	@Override
	public int hashCode() {
		return Objects.hash(success, recordsProcessed, errors);
	}
}

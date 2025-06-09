package com.simplesalesman.dto;

import java.util.List;

public class ImportResultDto {

	private boolean success;
	private int recordsProcessed;
	private List<String> errors;

	// Getter und Setter
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getRecordsProcessed() {
		return recordsProcessed;
	}

	public void setRecordsProcessed(int recordsProcessed) {
		this.recordsProcessed = recordsProcessed;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}

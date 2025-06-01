package com.simplesalesman.dto;

public class StatusUpdateDto {

    private Long projectId;
    private String newStatus;
    
    // Getter und Setter
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}


    
    
}

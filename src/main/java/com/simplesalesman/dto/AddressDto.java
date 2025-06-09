package com.simplesalesman.dto;

import java.util.List;

public class AddressDto {

    private Long id;
    private String addressText;
    private String regionName;

    private List<ProjectDto> projects;
    private List<NoteDto> notes;
	
    
    // Getter und Setter
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAddressText() {
		return addressText;
	}
	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public List<ProjectDto> getProjects() {
		return projects;
	}
	public void setProjects(List<ProjectDto> projects) {
		this.projects = projects;
	}
	public List<NoteDto> getNotes() {
		return notes;
	}
	public void setNotes(List<NoteDto> notes) {
		this.notes = notes;
	}

    
}

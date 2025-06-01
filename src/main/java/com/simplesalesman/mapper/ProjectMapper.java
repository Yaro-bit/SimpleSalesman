package com.simplesalesman.mapper;

import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectDto toDto(Project project) {
        if (project == null) {
            return null;
        }
    	ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setStatus(project.getStatus());
        dto.setOperator(project.getOperator());
        dto.setConstructionCompany(project.getConstructionCompany());
        dto.setPlannedConstructionEnd(project.getPlannedConstructionEnd());
        dto.setConstructionCompleted(project.isConstructionCompleted());
        dto.setSalesStart(project.getSalesStart());
        dto.setSalesEnd(project.getSalesEnd());
        dto.setNumberOfHomes(project.getNumberOfHomes());
        dto.setContractPresent(project.isContractPresent());
        dto.setCommissionCategory(project.getCommissionCategory());
        dto.setKgNumber(project.getKgNumber());
        dto.setProductPrice(project.getProductPrice());
        dto.setOutdoorFeePresent(project.isOutdoorFeePresent());
        return dto;
    }

    // Optional: toEntity() – z. B. beim Excel-Import
}

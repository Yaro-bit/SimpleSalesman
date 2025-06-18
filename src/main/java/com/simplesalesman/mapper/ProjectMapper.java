package com.simplesalesman.mapper;

import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.entity.Project;
import org.springframework.stereotype.Component;

/**
 * Mapper für Project <-> ProjectDto.
 * Achtung: toEntity() mapped nur Basisfelder, keine Address-Zuordnung.
 * Für Importe und einfache Konvertierung gedacht.
 * 
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.2
 */
@Component
public class ProjectMapper {

    public ProjectDto toDto(Project project) {
        if (project == null) return null;

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

    public Project toEntity(ProjectDto dto) {
        if (dto == null) return null;

        Project project = new Project();
        project.setId(dto.getId());
        project.setStatus(dto.getStatus());
        project.setOperator(dto.getOperator());
        project.setConstructionCompany(dto.getConstructionCompany());
        project.setPlannedConstructionEnd(dto.getPlannedConstructionEnd());
        project.setConstructionCompleted(dto.isConstructionCompleted());
        project.setSalesStart(dto.getSalesStart());
        project.setSalesEnd(dto.getSalesEnd());
        project.setNumberOfHomes(dto.getNumberOfHomes());
        project.setContractPresent(dto.isContractPresent());
        project.setCommissionCategory(dto.getCommissionCategory());
        project.setKgNumber(dto.getKgNumber());
        project.setProductPrice(dto.getProductPrice());
        project.setOutdoorFeePresent(dto.isOutdoorFeePresent());
        return project;
    }
}

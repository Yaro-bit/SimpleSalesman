package com.simplesalesman.mapper;

import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProjectMapperTest {

    private final ProjectMapper projectMapper = new ProjectMapper();

    @Test
    @DisplayName("toDto should return all values mapped correctly")
    void toDto_shouldMapAllFields() {
        Project project = new Project();
        project.setId(1L);
        project.setStatus("In Progress");
        project.setOperator("Alice");
        project.setConstructionCompany("ACME Bau");
        project.setPlannedConstructionEnd(LocalDate.of(2025, 8, 31));
        project.setConstructionCompleted(true);
        project.setSalesStart(LocalDate.of(2025, 1, 1));
        project.setSalesEnd(LocalDate.of(2025, 2, 1));
        project.setNumberOfHomes(5);
        project.setContractPresent(true);
        project.setCommissionCategory("A");
        project.setKgNumber("KG123");
        project.setProductPrice(new BigDecimal("1234.56"));
        project.setOutdoorFeePresent(false);

        ProjectDto dto = projectMapper.toDto(project);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo("In Progress");
        assertThat(dto.getOperator()).isEqualTo("Alice");
        assertThat(dto.getConstructionCompany()).isEqualTo("ACME Bau");
        assertThat(dto.getPlannedConstructionEnd()).isEqualTo(LocalDate.of(2025, 8, 31));
        assertThat(dto.isConstructionCompleted()).isTrue();
        assertThat(dto.getSalesStart()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(dto.getSalesEnd()).isEqualTo(LocalDate.of(2025, 2, 1));
        assertThat(dto.getNumberOfHomes()).isEqualTo(5);
        assertThat(dto.isContractPresent()).isTrue();
        assertThat(dto.getCommissionCategory()).isEqualTo("A");
        assertThat(dto.getKgNumber()).isEqualTo("KG123");
        assertThat(dto.getProductPrice()).isEqualByComparingTo("1234.56");
        assertThat(dto.isOutdoorFeePresent()).isFalse();
    }

    @Test
    @DisplayName("toDto should handle null input gracefully")
    void toDto_shouldReturnNull_whenProjectIsNull() {
        ProjectDto dto = projectMapper.toDto(null);
        assertNull(dto, "DTO should be null when input is null");
    }

    @Test
    @DisplayName("toDto should map empty Project object")
    void toDto_shouldMapEmptyProject() {
        Project emptyProject = new Project();
        ProjectDto dto = projectMapper.toDto(emptyProject);

        assertThat(dto.getId()).isNull();
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getOperator()).isNull();
        assertThat(dto.getConstructionCompany()).isNull();
        assertThat(dto.getPlannedConstructionEnd()).isNull();
        assertThat(dto.isConstructionCompleted()).isFalse();
        assertThat(dto.getSalesStart()).isNull();
        assertThat(dto.getSalesEnd()).isNull();
        assertThat(dto.getNumberOfHomes()).isZero();
        assertThat(dto.isContractPresent()).isFalse();
        assertThat(dto.getCommissionCategory()).isNull();
        assertThat(dto.getKgNumber()).isNull();
        assertThat(dto.getProductPrice()).isNull();
        assertThat(dto.isOutdoorFeePresent()).isFalse();
    }
}

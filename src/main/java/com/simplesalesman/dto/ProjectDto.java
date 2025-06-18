package com.simplesalesman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing project information linked to an
 * address.
 * 
 * This DTO contains all relevant metadata about a construction or sales
 * project, such as timeline, pricing, operator, and contract status. It is
 * primarily used for transferring project-related data between frontend and
 * backend.
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.3
 */
@Schema(description = "Project data linked to an address")
public class ProjectDto {

	private static final Logger logger = LoggerFactory.getLogger(ProjectDto.class);

	@Schema(description = "Unique project ID", example = "102")
	private Long id;

	@Schema(description = "Current status of the project", example = "OPEN")
	@NotBlank(message = "Project status must not be blank")
	private String status;

	@Schema(description = "Name of the project operator", example = "Muster Netz GmbH")
	private String operator;

	@Schema(description = "Construction company responsible for the project", example = "Bau AG")
	private String constructionCompany;

	@Schema(description = "Planned end date of the construction phase", example = "2025-10-15")
	private LocalDate plannedConstructionEnd;

	@Schema(description = "Indicates whether construction is completed", example = "false")
	private boolean constructionCompleted;

	@Schema(description = "Start date of the sales period", example = "2025-08-01")
	private LocalDate salesStart;

	@Schema(description = "End date of the sales period", example = "2026-01-31")
	private LocalDate salesEnd;

	@Schema(description = "Number of homes in the project", example = "32")
	@PositiveOrZero(message = "Number of homes must not be negative")
	private int numberOfHomes;

	@Schema(description = "Indicates whether a contract is present", example = "true")
	private boolean contractPresent;

	@Schema(description = "Commission category code", example = "C2")
	private String commissionCategory;

	@Schema(description = "KG (cadastral) number", example = "40220")
	private String kgNumber;

	@Schema(description = "Product price offered in the project", example = "1299.99")
	@DecimalMin(value = "0.0", inclusive = true, message = "Product price must not be negative")
	private BigDecimal productPrice;

	@Schema(description = "Indicates whether an outdoor fee applies", example = "true")
	private boolean outdoorFeePresent;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		logger.debug("ProjectDto ID set to {}", id);
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		logger.debug("ProjectDto status set to '{}'", status);
		this.status = status;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		logger.debug("ProjectDto operator set to '{}'", operator);
		this.operator = operator;
	}

	public String getConstructionCompany() {
		return constructionCompany;
	}

	public void setConstructionCompany(String constructionCompany) {
		logger.debug("ProjectDto constructionCompany set to '{}'", constructionCompany);
		this.constructionCompany = constructionCompany;
	}

	public LocalDate getPlannedConstructionEnd() {
		return plannedConstructionEnd;
	}

	public void setPlannedConstructionEnd(LocalDate plannedConstructionEnd) {
		logger.debug("ProjectDto plannedConstructionEnd set to {}", plannedConstructionEnd);
		this.plannedConstructionEnd = plannedConstructionEnd;
	}

	public boolean isConstructionCompleted() {
		return constructionCompleted;
	}

	public void setConstructionCompleted(boolean constructionCompleted) {
		logger.debug("ProjectDto constructionCompleted set to {}", constructionCompleted);
		this.constructionCompleted = constructionCompleted;
	}

	public LocalDate getSalesStart() {
		return salesStart;
	}

	public void setSalesStart(LocalDate salesStart) {
		logger.debug("ProjectDto salesStart set to {}", salesStart);
		this.salesStart = salesStart;
	}

	public LocalDate getSalesEnd() {
		return salesEnd;
	}

	public void setSalesEnd(LocalDate salesEnd) {
		logger.debug("ProjectDto salesEnd set to {}", salesEnd);
		this.salesEnd = salesEnd;
	}

	public int getNumberOfHomes() {
		return numberOfHomes;
	}

	public void setNumberOfHomes(int numberOfHomes) {
		logger.debug("ProjectDto numberOfHomes set to {}", numberOfHomes);
		this.numberOfHomes = numberOfHomes;
	}

	public boolean isContractPresent() {
		return contractPresent;
	}

	public void setContractPresent(boolean contractPresent) {
		logger.debug("ProjectDto contractPresent set to {}", contractPresent);
		this.contractPresent = contractPresent;
	}

	public String getCommissionCategory() {
		return commissionCategory;
	}

	public void setCommissionCategory(String commissionCategory) {
		logger.debug("ProjectDto commissionCategory set to '{}'", commissionCategory);
		this.commissionCategory = commissionCategory;
	}

	public String getKgNumber() {
		return kgNumber;
	}

	public void setKgNumber(String kgNumber) {
		logger.debug("ProjectDto kgNumber set to '{}'", kgNumber);
		this.kgNumber = kgNumber;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		logger.debug("ProjectDto productPrice set to {}", productPrice);
		this.productPrice = productPrice;
	}

	public boolean isOutdoorFeePresent() {
		return outdoorFeePresent;
	}

	public void setOutdoorFeePresent(boolean outdoorFeePresent) {
		logger.debug("ProjectDto outdoorFeePresent set to {}", outdoorFeePresent);
		this.outdoorFeePresent = outdoorFeePresent;
	}



	@Override
	public String toString() {
		return String.format("ProjectDto{id=%d, status='%s', homes=%d, operator='%s', price=%s}", id, status,
				numberOfHomes, operator, productPrice);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ProjectDto))
			return false;
		ProjectDto that = (ProjectDto) o;
		return constructionCompleted == that.constructionCompleted && numberOfHomes == that.numberOfHomes
				&& contractPresent == that.contractPresent && outdoorFeePresent == that.outdoorFeePresent
				&& Objects.equals(id, that.id) && Objects.equals(status, that.status)
				&& Objects.equals(operator, that.operator)
				&& Objects.equals(constructionCompany, that.constructionCompany)
				&& Objects.equals(plannedConstructionEnd, that.plannedConstructionEnd)
				&& Objects.equals(salesStart, that.salesStart) && Objects.equals(salesEnd, that.salesEnd)
				&& Objects.equals(commissionCategory, that.commissionCategory)
				&& Objects.equals(kgNumber, that.kgNumber) && Objects.equals(productPrice, that.productPrice);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, status, operator, constructionCompany, plannedConstructionEnd, constructionCompleted,
				salesStart, salesEnd, numberOfHomes, contractPresent, commissionCategory, kgNumber, productPrice,
				outdoorFeePresent);
	}
}

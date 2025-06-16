package com.simplesalesman.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing project information linked to an address.
 *
 * This DTO contains all relevant fields describing a construction or sales project,
 * including status, timeline, financial details, and metadata.
 *
 * Used for transferring project data between backend and frontend in the SimpleSalesman application.
 *
 * Author: SimpleSalesman Team
 * @version 0.0.5
 * @since 0.0.3
 */
public class ProjectDto {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDto.class);

    private Long id;
    private String status;
    private String operator;
    private String constructionCompany;

    private LocalDate plannedConstructionEnd;
    private boolean constructionCompleted;

    private LocalDate salesStart;
    private LocalDate salesEnd;

    private int numberOfHomes;
    private boolean contractPresent;
    private String commissionCategory;
    private String kgNumber;

    private BigDecimal productPrice;
    private boolean outdoorFeePresent;

    // === Getters and Setters ===

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
}

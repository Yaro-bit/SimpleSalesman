package com.simplesalesman.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectDto {

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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getConstructionCompany() {
		return constructionCompany;
	}
	public void setConstructionCompany(String constructionCompany) {
		this.constructionCompany = constructionCompany;
	}
	public LocalDate getPlannedConstructionEnd() {
		return plannedConstructionEnd;
	}
	public void setPlannedConstructionEnd(LocalDate plannedConstructionEnd) {
		this.plannedConstructionEnd = plannedConstructionEnd;
	}
	public boolean isConstructionCompleted() {
		return constructionCompleted;
	}
	public void setConstructionCompleted(boolean constructionCompleted) {
		this.constructionCompleted = constructionCompleted;
	}
	public LocalDate getSalesStart() {
		return salesStart;
	}
	public void setSalesStart(LocalDate salesStart) {
		this.salesStart = salesStart;
	}
	public LocalDate getSalesEnd() {
		return salesEnd;
	}
	public void setSalesEnd(LocalDate salesEnd) {
		this.salesEnd = salesEnd;
	}
	public int getNumberOfHomes() {
		return numberOfHomes;
	}
	public void setNumberOfHomes(int numberOfHomes) {
		this.numberOfHomes = numberOfHomes;
	}
	public boolean isContractPresent() {
		return contractPresent;
	}
	public void setContractPresent(boolean contractPresent) {
		this.contractPresent = contractPresent;
	}
	public String getCommissionCategory() {
		return commissionCategory;
	}
	public void setCommissionCategory(String commissionCategory) {
		this.commissionCategory = commissionCategory;
	}
	public String getKgNumber() {
		return kgNumber;
	}
	public void setKgNumber(String kgNumber) {
		this.kgNumber = kgNumber;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	public boolean isOutdoorFeePresent() {
		return outdoorFeePresent;
	}
	public void setOutdoorFeePresent(boolean outdoorFeePresent) {
		this.outdoorFeePresent = outdoorFeePresent;
	}

    // Getter und Setter
    
    
}

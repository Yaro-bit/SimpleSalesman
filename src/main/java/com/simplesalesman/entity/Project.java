package com.simplesalesman.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * JPA Entity representing a project assigned to an address in the
 * SimpleSalesman application.
 *
 * Each project contains structured information about a specific construction or
 * sales effort linked to a location. It supports tracking of construction
 * progress, sales timelines, contract details, pricing, and categorization.
 *
 * Relationships: - ManyToOne: Address (each project is tied to one address)
 *
 * Data fields include: - Construction status and company info - Sales phase
 * (start and end) - Commission and price details - Administrative data (e.g. KG
 * number, operator, category)
 *
 * Example use case: - Project "A45 Oberfeldstraße" has 30 homes, sales start in
 * Q2/2025, ANO is the operator, product price is €799.00, contract is present.
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.1
 */
@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String status;

	private String operator; // z. B. ANO
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

	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address address;

	// Getter und Setter...

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Project))
			return false;
		Project other = (Project) o;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}

package com.simplesalesman.entity;

import jakarta.persistence.*;
import java.util.List;
/**
 * JPA Entity representing a geographic or administrative region grouping multiple addresses.
 *
 * A region serves as a container for addresses and is used for organizational,
 * reporting, and filtering purposes in the SimpleSalesman application.
 *
 * Relationships:
 * - OneToMany: Address (each region may contain many addresses)
 *
 * Example:
 * - Region name: "Wels Land 92018-001"
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.1
 */
@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // z. B. "Adlwang 92018-011"

    @OneToMany(mappedBy = "region")
    private List<Address> addresses;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof Region)) return false;
	    Region other = (Region) o;
	    return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
	    return id != null ? id.hashCode() : 0;
	}
    
    
}

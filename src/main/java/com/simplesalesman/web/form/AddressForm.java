package com.simplesalesman.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Form DTO für Adress-Formulare in der Web-Oberfläche.
 * 
 * Optimiert für HTML-Formulare mit Web-spezifischen Validierungen.
 */
public class AddressForm {

    private Long id;

    @NotBlank(message = "Adresstext ist erforderlich")
    @Size(min = 5, max = 500, message = "Adresstext muss zwischen 5 und 500 Zeichen lang sein")
    private String addressText;

    @Size(max = 100, message = "Regionsname darf maximal 100 Zeichen lang sein")
    private String regionName;

    // === Constructors ===

    public AddressForm() {
    }

    public AddressForm(String addressText, String regionName) {
        this.addressText = addressText;
        this.regionName = regionName;
    }

    // === Getters & Setters ===

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

    // === Utility Methods ===

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return String.format("AddressForm{id=%d, addressText='%s', regionName='%s'}", 
                           id, addressText, regionName);
    }
}
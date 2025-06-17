package com.simplesalesman.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Form DTO für Notiz-Formulare in der Web-Oberfläche.
 * 
 * Optimiert für Textarea-Eingabe mit entsprechenden Validierungen.
 */
public class NoteForm {

    private Long id;
    
    private Long addressId;

    @NotBlank(message = "Notiztext ist erforderlich")
    @Size(min = 1, max = 2000, message = "Notiztext darf maximal 2000 Zeichen lang sein")
    private String text;

    @NotBlank(message = "Ersteller ist erforderlich")
    @Size(max = 100, message = "Ersteller-Name darf maximal 100 Zeichen lang sein")
    private String createdBy;

    // === Constructors ===

    public NoteForm() {
    }

    public NoteForm(Long addressId, String text, String createdBy) {
        this.addressId = addressId;
        this.text = text;
        this.createdBy = createdBy;
    }

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // === Utility Methods ===

    public boolean isNew() {
        return id == null;
    }

    public int getTextLength() {
        return text != null ? text.length() : 0;
    }

    @Override
    public String toString() {
        return String.format("NoteForm{id=%d, addressId=%d, textLength=%d, createdBy='%s'}", 
                           id, addressId, getTextLength(), createdBy);
    }
}
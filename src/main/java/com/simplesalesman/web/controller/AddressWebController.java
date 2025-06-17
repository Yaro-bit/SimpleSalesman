package com.simplesalesman.web.controller;

import com.simplesalesman.dto.AddressDto;
import com.simplesalesman.service.AddressService;
import com.simplesalesman.web.form.AddressForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

/**
 * MVC Controller für Adressverwaltung in der Web-Oberfläche.
 * 
 * Stellt HTML-Views für CRUD-Operationen bereit.
 */
@Controller
@RequestMapping("/web/addresses")
public class AddressWebController {

    private static final Logger log = LoggerFactory.getLogger(AddressWebController.class);
    private final AddressService addressService;

    public AddressWebController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * Zeigt alle Adressen in einer Tabelle an.
     */
    @GetMapping
    public String listAddresses(Model model) {
        log.info("Loading addresses list view");
        model.addAttribute("addresses", addressService.getAllAddresses());
        return "addresses/list";
    }

    /**
     * Zeigt das Formular für eine neue Adresse.
     */
    @GetMapping("/new")
    public String newAddressForm(Model model) {
        log.info("Loading new address form");
        model.addAttribute("addressForm", new AddressForm());
        model.addAttribute("isEdit", false);
        return "addresses/form";
    }

    /**
     * Zeigt das Formular zum Bearbeiten einer bestehenden Adresse.
     */
    @GetMapping("/{id}/edit")
    public String editAddressForm(@PathVariable Long id, Model model) {
        log.info("Loading edit form for address ID: {}", id);
        AddressDto address = addressService.getAddressById(id);
        if (address == null) {
            log.warn("Address with ID {} not found for editing", id);
            return "redirect:/web/addresses?error=notfound";
        }
        
        AddressForm form = new AddressForm();
        form.setId(address.getId());
        form.setAddressText(address.getAddressText());
        form.setRegionName(address.getRegionName());
        
        model.addAttribute("addressForm", form);
        model.addAttribute("isEdit", true);
        return "addresses/form";
    }

    /**
     * Verarbeitet das Erstellen einer neuen Adresse.
     */
    @PostMapping
    public String createAddress(@Valid @ModelAttribute AddressForm addressForm, 
                               BindingResult result, 
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.warn("Address form validation failed: {}", result.getAllErrors());
            return "addresses/form";
        }

        try {
            AddressDto dto = new AddressDto();
            dto.setAddressText(addressForm.getAddressText());
            dto.setRegionName(addressForm.getRegionName());
            
            AddressDto saved = addressService.createAddress(dto);
            log.info("Address created successfully with ID: {}", saved.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Adresse erfolgreich erstellt!");
            return "redirect:/web/addresses";
        } catch (Exception e) {
            log.error("Error creating address", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Fehler beim Erstellen der Adresse: " + e.getMessage());
            return "redirect:/web/addresses/new";
        }
    }

    /**
     * Verarbeitet das Aktualisieren einer bestehenden Adresse.
     */
    @PostMapping("/{id}")
    public String updateAddress(@PathVariable Long id,
                               @Valid @ModelAttribute AddressForm addressForm,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.warn("Address form validation failed for update: {}", result.getAllErrors());
            return "addresses/form";
        }

        try {
            AddressDto dto = new AddressDto();
            dto.setId(id);
            dto.setAddressText(addressForm.getAddressText());
            dto.setRegionName(addressForm.getRegionName());
            
            AddressDto updated = addressService.updateAddress(id, dto);
            if (updated != null) {
                log.info("Address updated successfully: {}", id);
                redirectAttributes.addFlashAttribute("successMessage", "Adresse erfolgreich aktualisiert!");
            } else {
                log.warn("Address with ID {} not found for update", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Adresse nicht gefunden!");
            }
            return "redirect:/web/addresses";
        } catch (Exception e) {
            log.error("Error updating address", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Fehler beim Aktualisieren: " + e.getMessage());
            return "redirect:/web/addresses/" + id + "/edit";
        }
    }

    /**
     * Löscht eine Adresse.
     */
    @PostMapping("/{id}/delete")
    public String deleteAddress(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = addressService.deleteAddress(id);
            if (deleted) {
                log.info("Address deleted successfully: {}", id);
                redirectAttributes.addFlashAttribute("successMessage", "Adresse erfolgreich gelöscht!");
            } else {
                log.warn("Address with ID {} not found for deletion", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Adresse nicht gefunden!");
            }
        } catch (Exception e) {
            log.error("Error deleting address", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Fehler beim Löschen: " + e.getMessage());
        }
        return "redirect:/web/addresses";
    }

    /**
     * Zeigt Details einer Adresse mit Projekten und Notizen.
     */
    @GetMapping("/{id}")
    public String viewAddress(@PathVariable Long id, Model model) {
        log.info("Loading address details for ID: {}", id);
        AddressDto address = addressService.getAddressById(id);
        if (address == null) {
            log.warn("Address with ID {} not found", id);
            return "redirect:/web/addresses?error=notfound";
        }
        
        model.addAttribute("address", address);
        return "addresses/detail";
    }
}
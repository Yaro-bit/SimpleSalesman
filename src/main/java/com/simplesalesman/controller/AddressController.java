package com.simplesalesman.controller;

import com.simplesalesman.dto.AddressDto;
import com.simplesalesman.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@CrossOrigin(origins = "*") // Für Webfrontend-Anbindung
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // GET /api/v1/addresses
    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    // GET /api/v1/addresses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long id) {
        AddressDto dto = addressService.getAddressById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    // POST /api/v1/addresses
    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDto) {
        AddressDto saved = addressService.createAddress(addressDto);
        return ResponseEntity.ok(saved);
    }

    // PUT /api/v1/addresses/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        AddressDto updated = addressService.updateAddress(id, addressDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // DELETE /api/v1/addresses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        boolean deleted = addressService.deleteAddress(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

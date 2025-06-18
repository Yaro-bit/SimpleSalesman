package com.simplesalesman.controller;

import com.simplesalesman.dto.AddressDto;
import com.simplesalesman.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for managing address operations in the SimpleSalesman application.
 *
 * This controller provides a complete CRUD interface for address management,
 * handling HTTP requests and responses for address-related operations.
 * All operations include comprehensive logging for monitoring and debugging.
 *
 * Key features:
 * - Full CRUD operations (Create, Read, Update, Delete)
 * - Comprehensive error handling with appropriate HTTP status codes
 * - Structured logging with different levels (INFO, WARN, ERROR, DEBUG)
 * - Input validation using Bean Validation annotations
 * - Cross-origin support for web frontend integration
 *
 * API Endpoints:
 * - GET /api/v1/addresses - Retrieve all addresses
 * - GET /api/v1/addresses/{id} - Retrieve specific address by ID
 * - POST /api/v1/addresses - Create new address
 * - PUT /api/v1/addresses/{id} - Update existing address
 * - DELETE /api/v1/addresses/{id} - Delete address by ID
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.4
 */
@RestController
@RequestMapping("/api/v1/addresses")
@CrossOrigin(origins = "*") // Für Webfrontend-Anbindung
public class AddressController {
    
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
    private final AddressService addressService;

    /**
     * Constructor for AddressController.
     *
     * @param addressService The service layer component for address operations
     */
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
        logger.info("AddressController initialized");
    }

    /**
     * Retrieves all addresses from the system.
     *
     * @return ResponseEntity containing a list of all AddressDto objects
     *         Returns HTTP 200 (OK) with the address list on success
     *         Returns HTTP 500 (Internal Server Error) if an exception occurs
     */
    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        logger.info("GET request received for all addresses");
        
        try {
            List<AddressDto> addresses = addressService.getAllAddresses();
            logger.info("Successfully retrieved {} addresses", addresses.size());
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            logger.error("Error retrieving all addresses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific address by its ID.
     *
     * @param id The unique identifier of the address to retrieve
     * @return ResponseEntity containing the AddressDto if found
     *         Returns HTTP 200 (OK) with the address data on success
     *         Returns HTTP 404 (Not Found) if the address doesn't exist
     *         Returns HTTP 500 (Internal Server Error) if an exception occurs
     */
    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long id) {
        logger.info("GET request received for address with ID: {}", id);
        
        try {
            AddressDto dto = addressService.getAddressById(id);
            if (dto != null) {
                logger.info("Successfully retrieved address with ID: {}", id);
                return ResponseEntity.ok(dto);
            } else {
                logger.warn("Address with ID {} not found", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving address with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new address in the system.
     *
     * @param addressDto The address data to be created (validated)
     * @return ResponseEntity containing the created AddressDto with generated ID
     *         Returns HTTP 201 (Created) with the new address data on success
     *         Returns HTTP 400 (Bad Request) if validation fails
     *         Returns HTTP 500 (Internal Server Error) if an exception occurs
     */
    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto) {
        logger.info("POST request received to create new address");
        logger.debug("Address data: {}", addressDto);
        
        try {
            AddressDto saved = addressService.createAddress(addressDto);
            logger.info("Successfully created address with ID: {}", saved.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("Error creating address", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing address with new data.
     *
     * @param id The unique identifier of the address to update
     * @param addressDto The new address data (validated)
     * @return ResponseEntity containing the updated AddressDto
     *         Returns HTTP 200 (OK) with the updated address data on success
     *         Returns HTTP 404 (Not Found) if the address doesn't exist
     *         Returns HTTP 400 (Bad Request) if validation fails
     *         Returns HTTP 500 (Internal Server Error) if an exception occurs
     */
    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, 
                                                   @Valid @RequestBody AddressDto addressDto) {
        logger.info("PUT request received to update address with ID: {}", id);
        logger.debug("Updated address data: {}", addressDto);
        
        try {
            AddressDto updated = addressService.updateAddress(id, addressDto);
            if (updated != null) {
                logger.info("Successfully updated address with ID: {}", id);
                return ResponseEntity.ok(updated);
            } else {
                logger.warn("Address with ID {} not found for update", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error updating address with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes an address from the system.
     *
     * @param id The unique identifier of the address to delete
     * @return ResponseEntity with no content
     *         Returns HTTP 204 (No Content) if deletion was successful
     *         Returns HTTP 404 (Not Found) if the address doesn't exist
     *         Returns HTTP 500 (Internal Server Error) if an exception occurs
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        logger.info("DELETE request received for address with ID: {}", id);
        
        try {
            boolean deleted = addressService.deleteAddress(id);
            if (deleted) {
                logger.info("Successfully deleted address with ID: {}", id);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("Address with ID {} not found for deletion", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting address with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
// 3. Controller Class (VersionController.java)
package com.simplesalesman.exam.version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing version operations in the SimpleSalesman application.
 *
 * Simple CRUD controller for t_version table following AddressController pattern
 * but without service layer complexity.
 *
 * @author SimpleSalesman Team
 * @version 0.0.9
 * @since 0.0.9
 */
@RestController
@RequestMapping("/api/v1/versions")
@CrossOrigin(origins = "*")
public class VersionController {
    
    private static final Logger logger = LoggerFactory.getLogger(VersionController.class);
    private final VersionRepository versionRepository;

    public VersionController(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
        logger.info("VersionController initialized");
    }

    /**
     * Retrieves all versions from t_version table.
     */
    @GetMapping
    public ResponseEntity<List<Version>> getAllVersions() {
        logger.info("GET request received for all versions");
        
        try {
            List<Version> versions = versionRepository.findAll();
            logger.info("Successfully retrieved {} versions", versions.size());
            return ResponseEntity.ok(versions);
        } catch (Exception e) {
            logger.error("Error retrieving all versions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific version by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Version> getVersionById(@PathVariable Long id) {
        logger.info("GET request received for version with ID: {}", id);
        
        try {
            Optional<Version> version = versionRepository.findById(id);
            if (version.isPresent()) {
                logger.info("Successfully retrieved version with ID: {}", id);
                return ResponseEntity.ok(version.get());
            } else {
                logger.warn("Version with ID {} not found", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving version with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new version in t_version table.
     */
    @PostMapping
    public ResponseEntity<Version> createVersion(@Valid @RequestBody Version version) {
        logger.info("POST request received to create new version");
        logger.debug("Version data: {}", version.getVersion());
        
        try {
            Version saved = versionRepository.save(version);
            logger.info("Successfully created version with ID: {}", saved.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("Error creating version", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing version.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Version> updateVersion(@PathVariable Long id, 
                                               @Valid @RequestBody Version versionDetails) {
        logger.info("PUT request received to update version with ID: {}", id);
        logger.debug("Updated version data: {}", versionDetails.getVersion());
        
        try {
            Optional<Version> optionalVersion = versionRepository.findById(id);
            if (optionalVersion.isPresent()) {
                Version version = optionalVersion.get();
                version.setVersion(versionDetails.getVersion());
                Version updated = versionRepository.save(version);
                logger.info("Successfully updated version with ID: {}", id);
                return ResponseEntity.ok(updated);
            } else {
                logger.warn("Version with ID {} not found for update", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error updating version with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a version from t_version table.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVersion(@PathVariable Long id) {
        logger.info("DELETE request received for version with ID: {}", id);
        
        try {
            if (versionRepository.existsById(id)) {
                versionRepository.deleteById(id);
                logger.info("Successfully deleted version with ID: {}", id);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("Version with ID {} not found for deletion", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting version with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
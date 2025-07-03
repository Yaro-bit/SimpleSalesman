// 3. Controller Class (SampleController.java)
package com.simplesalesman.exam.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/samples")
@CrossOrigin(origins = "*")
public class SampleController {
    
    private static final Logger logger = LoggerFactory.getLogger(SampleController.class);
    private final SampleRepository sampleRepository;

    public SampleController(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
        logger.info("SampleController initialized");
    }

    @GetMapping
    public ResponseEntity<List<Sample>> getAllSamples() {
        logger.info("GET request for all samples");
        try {
            List<Sample> samples = sampleRepository.findAll();
            logger.info("Retrieved {} samples", samples.size());
            return ResponseEntity.ok(samples);
        } catch (Exception e) {
            logger.error("Error retrieving samples", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sample> getSampleById(@PathVariable Long id) {
        logger.info("GET request for sample ID: {}", id);
        try {
            Optional<Sample> sample = sampleRepository.findById(id);
            if (sample.isPresent()) {
                return ResponseEntity.ok(sample.get());
            } else {
                logger.warn("Sample ID {} not found", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving sample ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Sample> createSample(@Valid @RequestBody Sample sample) {
        logger.info("POST request to create sample");
        try {
            Sample saved = sampleRepository.save(sample);
            logger.info("Created sample with ID: {}", saved.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("Error creating sample", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sample> updateSample(@PathVariable Long id, @Valid @RequestBody Sample sampleDetails) {
        logger.info("PUT request to update sample ID: {}", id);
        try {
            Optional<Sample> optionalSample = sampleRepository.findById(id);
            if (optionalSample.isPresent()) {
                Sample sample = optionalSample.get();
                sample.setName(sampleDetails.getName());
                Sample updated = sampleRepository.save(sample);
                logger.info("Updated sample ID: {}", id);
                return ResponseEntity.ok(updated);
            } else {
                logger.warn("Sample ID {} not found for update", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error updating sample ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSample(@PathVariable Long id) {
        logger.info("DELETE request for sample ID: {}", id);
        try {
            if (sampleRepository.existsById(id)) {
                sampleRepository.deleteById(id);
                logger.info("Deleted sample ID: {}", id);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("Sample ID {} not found for deletion", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting sample ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
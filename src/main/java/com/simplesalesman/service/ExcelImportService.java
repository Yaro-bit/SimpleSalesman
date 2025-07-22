package com.simplesalesman.service;

import com.simplesalesman.dto.ImportResultDto;
import com.simplesalesman.entity.*;
import com.simplesalesman.repository.*;
import com.simplesalesman.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ExcelImportService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelImportService.class);
    private static final int BATCH_SIZE = 1000; // Process in batches for large files

    private final RegionRepository regionRepository;
    private final AddressRepository addressRepository;
    private final ProjectRepository projectRepository;
    private final ExcelUtil excelUtil;

    public ExcelImportService(RegionRepository regionRepository,
                               AddressRepository addressRepository,
                               ProjectRepository projectRepository,
                               ExcelUtil excelUtil) {
        this.regionRepository = regionRepository;
        this.addressRepository = addressRepository;
        this.projectRepository = projectRepository;
        this.excelUtil = excelUtil;
    }

    @Transactional(rollbackFor = Exception.class)
    public ImportResultDto importExcel(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int recordsProcessed = 0;
        int recordsSkipped = 0;

        try {
            logger.info("Starting Excel import for file: {}, size: {} MB", 
                file.getOriginalFilename(), file.getSize() / (1024.0 * 1024.0));
            
            // Parse Excel file
            List<Project> projects = excelUtil.parse(file.getInputStream());
            logger.info("Parsed {} projects from Excel", projects.size());

            // Load existing regions and addresses for duplicate checking
            Map<String, Region> regionCache = loadRegionCache();
            Set<String> existingAddresses = loadExistingAddresses();
            
            // Process new regions first
            Set<Region> newRegions = processNewRegions(projects, regionCache);
            if (!newRegions.isEmpty()) {
                List<Region> savedRegions = regionRepository.saveAll(newRegions);
                savedRegions.forEach(r -> regionCache.put(r.getName(), r));
                logger.info("Created {} new regions", savedRegions.size());
            }

            // Process in batches for better performance
            List<Address> addressesToSave = new ArrayList<>();
            List<Project> projectsToSave = new ArrayList<>();
            
            for (Project project : projects) {
                Address address = project.getAddress();
                
                // Skip duplicate addresses
                if (existingAddresses.contains(address.getAddressText())) {
                    recordsSkipped++;
                    errors.add("Duplicate address skipped: " + address.getAddressText());
                    continue;
                }
                
                // Set the cached region
                Region cachedRegion = regionCache.get(address.getRegion().getName());
                if (cachedRegion == null) {
                    errors.add("Region not found for address: " + address.getAddressText());
                    recordsSkipped++;
                    continue;
                }
                
                address.setRegion(cachedRegion);
                addressesToSave.add(address);
                projectsToSave.add(project);
                
                // Process batch if size reached
                if (addressesToSave.size() >= BATCH_SIZE) {
                    recordsProcessed += processBatch(addressesToSave, projectsToSave);
                    addressesToSave.clear();
                    projectsToSave.clear();
                }
            }
            
            // Process remaining records
            if (!addressesToSave.isEmpty()) {
                recordsProcessed += processBatch(addressesToSave, projectsToSave);
            }
            
            logger.info("Import completed. Processed: {}, Skipped: {}", recordsProcessed, recordsSkipped);

        } catch (Exception e) {
            logger.error("Import failed with error", e);
            errors.add("Import failed: " + e.getMessage());
            throw new RuntimeException("Import failed", e); // Trigger rollback
        }

        // Build result
        ImportResultDto result = new ImportResultDto();
        result.setSuccess(errors.isEmpty() || recordsProcessed > 0);
        result.setRecordsProcessed(recordsProcessed);
        result.setErrors(errors);
        return result;
    }

    private Map<String, Region> loadRegionCache() {
        return regionRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(
                    Region::getName, 
                    r -> r,
                    (existing, replacement) -> existing, // Keep existing on duplicates
                    ConcurrentHashMap::new
                ));
    }

    private Set<String> loadExistingAddresses() {
        // Use projection for better performance - only load addressText
        return new HashSet<>(addressRepository.findAllAddressTexts());
    }

    private Set<Region> processNewRegions(List<Project> projects, Map<String, Region> regionCache) {
        return projects.stream()
                .map(p -> p.getAddress().getRegion())
                .filter(r -> !regionCache.containsKey(r.getName()))
                .collect(Collectors.toMap(
                    Region::getName,
                    r -> r,
                    (r1, r2) -> r1 // Keep first on duplicates
                ))
                .values()
                .stream()
                .collect(Collectors.toSet());
    }

    private int processBatch(List<Address> addresses, List<Project> projects) {
        // Save addresses
        List<Address> savedAddresses = addressRepository.saveAll(addresses);
        
        // Link saved addresses to projects
        for (int i = 0; i < projects.size(); i++) {
            projects.get(i).setAddress(savedAddresses.get(i));
        }
        
        // Save projects
        projectRepository.saveAll(projects);
        
        logger.debug("Processed batch of {} records", projects.size());
        return projects.size();
    }
}
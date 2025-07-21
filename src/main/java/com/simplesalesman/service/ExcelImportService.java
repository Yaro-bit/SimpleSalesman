package com.simplesalesman.service;

import com.simplesalesman.dto.ImportResultDto;
import com.simplesalesman.entity.*;
import com.simplesalesman.repository.*;
import com.simplesalesman.util.ExcelUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for importing project and address data from Excel files.
 *
 * Performance optimizations:
 * - Region caching to prevent O(n*m) database queries
 * - Batch processing with saveAll() for better performance
 * - Single transaction for consistency and rollback
 *
 * @author SimpleSalesman Team
 * @version 0.1.0
 */
@Service
public class ExcelImportService {

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

    /**
     * Imports Excel file with project data using optimized batch processing.
     *
     * @param file Excel file (.xlsx/.xls) containing project data
     * @return ImportResultDto with success status, records processed, and any errors
     */
    @Transactional(rollbackFor = Exception.class)
    public ImportResultDto importExcel(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int recordsProcessed = 0;

        try {
            // Parse Excel file into Project entities
            List<Project> projects = excelUtil.parse(file.getInputStream());

            // Cache all regions once to avoid repeated database calls (Performance Fix)
            Map<String, Region> regionCache = regionRepository.findAll().stream()
                    .collect(Collectors.toMap(Region::getName, Function.identity()));

            // Prepare batch collections for saveAll() operations
            List<Address> addressesToSave = new ArrayList<>();
            List<Project> projectsToSave = new ArrayList<>();

            for (Project project : projects) {
                Address address = project.getAddress();
                Region region = address.getRegion();

                // Use cached region or create new one if doesn't exist
                Region existingRegion = regionCache.computeIfAbsent(region.getName(), 
                    name -> {
                        Region newRegion = regionRepository.save(region);
                        return newRegion;
                    });

                address.setRegion(existingRegion);
                addressesToSave.add(address);
                projectsToSave.add(project);
            }

            // Batch save addresses first (required due to foreign key constraints)
            List<Address> savedAddresses = addressRepository.saveAll(addressesToSave);
            
            // Link saved addresses to projects
            for (int i = 0; i < projectsToSave.size(); i++) {
                projectsToSave.get(i).setAddress(savedAddresses.get(i));
            }

            // Batch save all projects
            projectRepository.saveAll(projectsToSave);
            recordsProcessed = projectsToSave.size();

        } catch (Exception e) {
            errors.add("Fehler beim Verarbeiten: " + e.getMessage());
        }

        // Build and return result summary
        ImportResultDto result = new ImportResultDto();
        result.setSuccess(errors.isEmpty());
        result.setRecordsProcessed(recordsProcessed);
        result.setErrors(errors);
        return result;
    }
}
package com.simplesalesman.service;

import com.simplesalesman.dto.ImportResultDto;
import com.simplesalesman.entity.*;
import com.simplesalesman.repository.*;
import com.simplesalesman.util.ExcelUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for importing project and address data from Excel files.
 *
 * This service handles the parsing and persistence of incoming Excel data using
 * a utility class {@link ExcelUtil} to extract {@link Project} entities and their
 * linked {@link Address} and {@link Region} objects.
 *
 * Responsibilities:
 * - Parse an uploaded Excel file into a list of projects
 * - Ensure referenced regions exist or are created
 * - Persist address and project entities in the correct order
 * - Report number of records processed and potential import errors
 *
 * Assumptions:
 * - Each row in the Excel file represents a complete project with one address and region
 * - Duplicate region names are handled by reusing the existing entity
 *
 * Dependencies:
 * - {@link RegionRepository}, {@link AddressRepository}, {@link ProjectRepository}
 * - {@link ExcelUtil} for Excel parsing logic
 *
 * Security Considerations:
 * - Input must be a valid Excel file (XLS/XLSX), enforced by {@code ExcelUtil}
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.5
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
     * Imports an Excel file containing project data.
     *
     * Steps:
     * - Parses Excel rows into {@link Project} objects
     * - Resolves or creates referenced {@link Region} entities
     * - Saves the related {@link Address} and links it to the project
     * - Saves each {@link Project}
     *
     * @param file the uploaded Excel file (expected to contain project data)
     * @return an {@link ImportResultDto} with import status, number of records, and errors
     */
    public ImportResultDto importExcel(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int recordsProcessed = 0;

        try {
            List<Project> projects = excelUtil.parse(file.getInputStream());

            for (Project project : projects) {
                Address address = project.getAddress();
                Region region = address.getRegion();

                // Reuse existing region or create new one
                Region existingRegion = regionRepository.findAll().stream()
                        .filter(r -> r.getName().equals(region.getName()))
                        .findFirst()
                        .orElseGet(() -> regionRepository.save(region));

                address.setRegion(existingRegion);

                // Save address and associate with project
                Address savedAddress = addressRepository.save(address);
                project.setAddress(savedAddress);

                projectRepository.save(project);
                recordsProcessed++;
            }
        } catch (Exception e) {
            errors.add("Fehler beim Verarbeiten: " + e.getMessage());
        }

        // Prepare result summary
        ImportResultDto result = new ImportResultDto();
        result.setSuccess(errors.isEmpty());
        result.setRecordsProcessed(recordsProcessed);
        result.setErrors(errors);
        return result;
    }
}

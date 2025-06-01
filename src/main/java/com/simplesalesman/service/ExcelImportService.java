package com.simplesalesman.service;

import com.simplesalesman.dto.ImportResultDto;
import com.simplesalesman.entity.*;
import com.simplesalesman.repository.*;
import com.simplesalesman.util.ExcelUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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

    public ImportResultDto importExcel(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int recordsProcessed = 0;

        try {
            List<Project> projects = excelUtil.parse(file.getInputStream());

            for (Project project : projects) {
                Address address = project.getAddress();
                Region region = address.getRegion();

                Region existingRegion = regionRepository.findAll().stream()
                        .filter(r -> r.getName().equals(region.getName()))
                        .findFirst()
                        .orElseGet(() -> regionRepository.save(region));

                address.setRegion(existingRegion);

                Address savedAddress = addressRepository.save(address);
                project.setAddress(savedAddress);

                projectRepository.save(project);
                recordsProcessed++;
            }
        } catch (Exception e) {
            errors.add("Fehler beim Verarbeiten: " + e.getMessage());
        }

        ImportResultDto result = new ImportResultDto();
        result.setSuccess(errors.isEmpty());
        result.setRecordsProcessed(recordsProcessed);
        result.setErrors(errors);
        return result;
    }
}

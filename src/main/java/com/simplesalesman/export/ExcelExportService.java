// 2. ExcelExportService.java
package com.simplesalesman.export;

import com.simplesalesman.entity.Project;
import com.simplesalesman.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for exporting project data to Excel files.
 *
 * @author SimpleSalesman Team
 * @version 0.0.9
 * @since 0.0.9
 */
@Service
public class ExcelExportService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);
    
    private final ProjectRepository projectRepository;
    private final ExcelExportUtil excelExportUtil;

    public ExcelExportService(ProjectRepository projectRepository, ExcelExportUtil excelExportUtil) {
        this.projectRepository = projectRepository;
        this.excelExportUtil = excelExportUtil;
        logger.info("ExcelExportService initialized");
    }

    /**
     * Exports all projects to Excel format.
     */
    public byte[] exportProjectsToExcel() throws Exception {
        logger.info("Starting export of all projects to Excel");
        
        List<Project> projects = projectRepository.findAll();
        logger.info("Retrieved {} projects from database", projects.size());
        
        if (projects.isEmpty()) {
            logger.warn("No projects found for export");
        }
        
        return excelExportUtil.createExcel(projects);
    }

    /**
     * Exports projects filtered by region to Excel format.
     */
    public byte[] exportProjectsByRegion(Long regionId) throws Exception {
        logger.info("Starting export of projects for region: {}", regionId);
        
        // Get all projects and filter by region in memory (simplest approach)
        List<Project> allProjects = projectRepository.findAll();
        List<Project> regionProjects = allProjects.stream()
            .filter(project -> project.getAddress() != null && 
                              project.getAddress().getRegion() != null && 
                              regionId.equals(project.getAddress().getRegion().getId()))
            .collect(java.util.stream.Collectors.toList());
        
        logger.info("Retrieved {} projects from database for region: {}", regionProjects.size(), regionId);
        
        if (regionProjects.isEmpty()) {
            logger.warn("No projects found for region: {}", regionId);
        }
        
        return excelExportUtil.createExcel(regionProjects);
    }
}
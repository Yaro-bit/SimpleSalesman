package com.simplesalesman.exam.export;

import com.simplesalesman.entity.Project;
import com.simplesalesman.entity.Note;
import com.simplesalesman.repository.ProjectRepository;
import com.simplesalesman.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for exporting project and note data to Excel files.
 *
 * Notes are linked to addresses, and projects are linked to addresses,
 * so we export projects with their address-associated notes.
 *
 * @author Yaroslav Volokhodko
 * @version exam
 * @since 0.0.9
 */
@Service
public class ExcelExportService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);

    private final ProjectRepository projectRepository;
    private final NoteRepository noteRepository;
    private final ExcelExportUtil excelExportUtil;

    public ExcelExportService(ProjectRepository projectRepository,
                              NoteRepository noteRepository,
                              ExcelExportUtil excelExportUtil) {
        this.projectRepository = projectRepository;
        this.noteRepository = noteRepository;
        this.excelExportUtil = excelExportUtil;
        logger.info("ExcelExportService initialized with projects and address-linked notes support");
    }

    /**
     * Exports all projects and their associated address notes to Excel format.
     */
    public byte[] exportProjectsAndNotesToExcel() throws Exception {
        logger.info("Starting export of all projects and address notes to Excel");

        List<Project> projects = projectRepository.findAll();

        List<Long> projectAddressIds = projects.stream()
                .filter(p -> p.getAddress() != null)
                .map(p -> p.getAddress().getId())
                .collect(Collectors.toList());

        List<Note> projectRelatedNotes = noteRepository.findByAddressIds(projectAddressIds);

        logger.info("Retrieved {} projects and {} project-related notes from database",
                projects.size(), projectRelatedNotes.size());

        if (projects.isEmpty() && projectRelatedNotes.isEmpty()) {
            logger.warn("No projects or notes found for export");
        }

        return excelExportUtil.createExcelWithProjectsAndNotes(projects, projectRelatedNotes);
    }

    /**
     * Exports projects and notes filtered by region to Excel format.
     */
    public byte[] exportProjectsAndNotesByRegion(Long regionId) throws Exception {
        logger.info("Starting export of projects and notes for region: {}", regionId);

        List<Project> allProjects = projectRepository.findAll();
        List<Project> regionProjects = allProjects.stream()
                .filter(project -> project.getAddress() != null &&
                        project.getAddress().getRegion() != null &&
                        regionId.equals(project.getAddress().getRegion().getId()))
                .collect(Collectors.toList());

        List<Long> regionAddressIds = regionProjects.stream()
                .filter(p -> p.getAddress() != null)
                .map(p -> p.getAddress().getId())
                .collect(Collectors.toList());

        List<Note> regionNotes = noteRepository.findByAddressIds(regionAddressIds);

        logger.info("Retrieved {} projects and {} notes from database for region: {}",
                regionProjects.size(), regionNotes.size(), regionId);

        if (regionProjects.isEmpty() && regionNotes.isEmpty()) {
            logger.warn("No projects or notes found for region: {}", regionId);
        }

        return excelExportUtil.createExcelWithProjectsAndNotes(regionProjects, regionNotes);
    }

    /**
     * Exports only projects to Excel format (legacy method).
     */
    public byte[] exportProjectsOnlyToExcel() throws Exception {
        logger.info("Starting export of projects only to Excel (legacy)");

        List<Project> projects = projectRepository.findAll();
        logger.info("Retrieved {} projects from database", projects.size());

        if (projects.isEmpty()) {
            logger.warn("No projects found for export");
        }

        return excelExportUtil.createExcelWithProjectsOnly(projects);
    }

    /**
     * Exports only notes to Excel format.
     */
    public byte[] exportNotesOnlyToExcel() throws Exception {
        logger.info("Starting export of notes only to Excel");

        List<Note> notes = noteRepository.findAll();
        logger.info("Retrieved {} notes from database", notes.size());

        if (notes.isEmpty()) {
            logger.warn("No notes found for export");
        }

        return excelExportUtil.createExcelWithNotesOnly(notes);
    }

    /**
     * Legacy method - exports all projects to Excel format.
     * @deprecated Use exportProjectsAndNotesToExcel() instead
     */
    @Deprecated
    public byte[] exportProjectsToExcel() throws Exception {
        logger.warn("Using deprecated method exportProjectsToExcel(). Consider using exportProjectsAndNotesToExcel()");
        return exportProjectsOnlyToExcel();
    }

    /**
     * Legacy method - exports projects filtered by region to Excel format.
     * @deprecated Use exportProjectsAndNotesByRegion() instead
     */
    @Deprecated
    public byte[] exportProjectsByRegion(Long regionId) throws Exception {
        logger.warn("Using deprecated method exportProjectsByRegion({}). Consider using exportProjectsAndNotesByRegion()", regionId);

        List<Project> allProjects = projectRepository.findAll();
        List<Project> regionProjects = allProjects.stream()
                .filter(project -> project.getAddress() != null &&
                        project.getAddress().getRegion() != null &&
                        regionId.equals(project.getAddress().getRegion().getId()))
                .collect(Collectors.toList());

        logger.info("Retrieved {} projects from database for region: {}", regionProjects.size(), regionId);

        if (regionProjects.isEmpty()) {
            logger.warn("No projects found for region: {}", regionId);
        }

        return excelExportUtil.createExcelWithProjectsOnly(regionProjects);
    }
}

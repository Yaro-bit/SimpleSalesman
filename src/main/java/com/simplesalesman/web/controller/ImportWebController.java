package com.simplesalesman.web.controller;

import com.simplesalesman.dto.ImportResultDto;
import com.simplesalesman.service.ExcelImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * MVC Controller für Excel-Import in der Web-Oberfläche.
 * 
 * Stellt HTML-Interface für File-Upload bereit.
 */
@Controller
@RequestMapping("/web/import")
public class ImportWebController {

    private static final Logger log = LoggerFactory.getLogger(ImportWebController.class);
    private final ExcelImportService excelImportService;

    public ImportWebController(ExcelImportService excelImportService) {
        this.excelImportService = excelImportService;
    }

    /**
     * Zeigt das Upload-Formular an.
     */
    @GetMapping
    public String showImportForm(Model model) {
        log.info("Loading import form");
        return "import/upload";
    }

    /**
     * Verarbeitet den Excel-Upload.
     */
    @PostMapping
    public String processImport(@RequestParam("file") MultipartFile file, 
                               Model model,
                               RedirectAttributes redirectAttributes) {
        log.info("Processing file upload: {}", file.getOriginalFilename());

        // Validierung
        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "Bitte wählen Sie eine Datei aus.");
            return "import/upload";
        }

        // Dateigröße prüfen (10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            model.addAttribute("errorMessage", "Datei ist zu groß. Maximale Größe: 10MB");
            return "import/upload";
        }

        // Dateiformat prüfen
        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.toLowerCase().endsWith(".xlsx") && !filename.toLowerCase().endsWith(".xls"))) {
            model.addAttribute("errorMessage", "Nur Excel-Dateien (.xlsx, .xls) sind erlaubt.");
            return "import/upload";
        }

        try {
            // Import durchführen
            ImportResultDto result = excelImportService.importExcel(file);

            if (result.isSuccess()) {
                redirectAttributes.addFlashAttribute("successMessage", 
                    String.format("Import erfolgreich! %d Datensätze verarbeitet.", result.getRecordsProcessed()));
                log.info("Import successful: {} records processed", result.getRecordsProcessed());
                return "redirect:/web/import/result";
            } else {
                model.addAttribute("result", result);
                model.addAttribute("errorMessage", "Import teilweise fehlgeschlagen.");
                log.warn("Import completed with errors: {} records processed, {} errors", 
                        result.getRecordsProcessed(), result.getErrors().size());
                return "import/upload";
            }

        } catch (Exception e) {
            log.error("Error during import", e);
            model.addAttribute("errorMessage", "Fehler beim Import: " + e.getMessage());
            return "import/upload";
        }
    }

    /**
     * Zeigt das Import-Ergebnis an.
     */
    @GetMapping("/result")
    public String showResult(Model model) {
        return "import/result";
    }
}
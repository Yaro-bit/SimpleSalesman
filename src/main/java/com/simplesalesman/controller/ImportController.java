package com.simplesalesman.controller;

import com.simplesalesman.dto.ImportResultDto;
import com.simplesalesman.service.ExcelImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/import")
@CrossOrigin(origins = "*")
public class ImportController {

    private final ExcelImportService excelImportService;

    public ImportController(ExcelImportService excelImportService) {
        this.excelImportService = excelImportService;
    }

    @PostMapping
    public ResponseEntity<ImportResultDto> importExcel(@RequestParam("file") MultipartFile file) {
        ImportResultDto result = excelImportService.importExcel(file);
        return ResponseEntity.ok(result);
    }
}

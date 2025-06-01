# 📄 ImportController.java

**Paket:** `com.simplesalesman.controller`

## Beschreibung
Verantwortlich für den Upload und die Initialverarbeitung von Excel-Dateien via Apache POI. Übergibt Daten an `ExcelImportService`.

## Hauptmethoden
- `POST /api/v1/import` – Datei-Upload

## Kommentar für DEV
> Schreibe einen Mock-Test mit einer echten Excel-Datei (z. B. aus Test-Resourcen) und prüfe die Rückgabe (`ImportResultDto`). Achte auf valide/ungültige Formate.
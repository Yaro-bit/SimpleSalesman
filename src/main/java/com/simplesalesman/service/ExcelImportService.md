# 📄 ExcelImportService.java

**Paket:** `com.simplesalesman.service`

## Beschreibung
Zentrale Komponente für den Excel-Import. Analysiert, parst und übergibt Datenstrukturen an andere Services (z. B. AddressService).

## Hauptfunktionen
- Einlesen von Excel-Dateien via Apache POI
- Asynchrone Verarbeitung mit Fehlerprotokoll
- Rückgabe eines strukturierten Importergebnisses

## Kommentar für DEV
> Teste den Import einer Datei mit gemischten Inhalten (valide/fehlerhafte Zeilen). Verifiziere die Rückgabe im `ImportResultDto`.
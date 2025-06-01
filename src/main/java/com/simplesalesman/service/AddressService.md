# 📄 AddressService.java

**Paket:** `com.simplesalesman.service`

## Beschreibung
Verarbeitet alle adressbezogenen Geschäftslogiken, wie Import, Matching, Validierung und Persistenz. Arbeitet eng mit `AddressRepository` und `ExcelImportService` zusammen.

## Hauptfunktionen
- Prüfung und Speicherung von Adressdaten
- Validierung von Pflichtfeldern und Duplikaten
- Abgleich bestehender Daten bei erneutem Import
- Verknüpfung mit Projekten und Regionen

## Kommentar für DEV
> Erstelle einen Unit-Test, der mehrere Adressen importiert. Überprüfe, ob Duplikate erkannt und bestehende Datensätze korrekt aktualisiert werden.
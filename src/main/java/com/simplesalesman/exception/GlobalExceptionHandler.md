# 📄 GlobalExceptionHandler.java

**Paket:** `com.simplesalesman.exception`

## Beschreibung
Globale Fehlerbehandlungs-Klasse mit `@ControllerAdvice`. Fängt definierte Ausnahmen wie z. B. `ExcelImportException` oder `DataValidationException` ab und wandelt sie in standardisierte HTTP-Antworten um.

## Typische Funktionen
- Fehlerlogging
- Rückgabe strukturierter Fehlerantworten
- Mapping von Exception-Typen zu HTTP-Codes

## Kommentar für DEV
> Simuliere das Auslösen verschiedener Exceptions im Controller und überprüfe, ob diese korrekt vom Handler abgefangen und als strukturierte Antwort dargestellt werden.
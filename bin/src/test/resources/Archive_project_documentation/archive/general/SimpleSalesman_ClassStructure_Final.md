
# Simple Salesman – Klassendiagramm (finale Struktur mit Bibliotheksbezug)

## 1. Controller-Schicht (`com.simplesalesman.controller`)
- `AddressController`
  - REST-Endpunkte zur Adressverwaltung
- `ProjectController`
  - Endpunkte für Projektinformationen & Statusupdates
- `NoteController`
  - Endpunkte zur Notizverarbeitung
- `ImportController`
  - Datei-Upload für Excel-Import
- `AuthController`
  - Nutzerinformationen (von Keycloak extrahiert)

## 2. Service-Schicht (`com.simplesalesman.service`)
- `AddressService`
  - Adresslogik (Import, Matching, Persistenz)
- `ProjectService`
  - Projektbearbeitung inkl. Statuslogik
- `NoteService`
  - Validierung und Speicherung von Notizen
- `ExcelImportService`
  - Apache POI Integration, asynchrone Verarbeitung
- `WeatherService`
  - REST-Aufruf externer Wetter-API (OpenWeatherMap)
- `UserService`
  - Keycloak-Integration: User-Details, Rollen

## 3. Repository-Schicht (`com.simplesalesman.repo`)
- `AddressRepository`
- `ProjectRepository`
- `NoteRepository`
- `RegionRepository`
- `StatusRepository`

## 4. Entity-Schicht (`com.simplesalesman.entity`)
- `Address`
  - region, notizen, linked projects
- `Project`
  - status, operator, construction company
- `Note`
  - text, timestamp, createdBy
- `Region`
  - PLZ-Gebiete zur Gruppierung
- `Status`
  - Enum-artige Entität für Projektstatus
- `User`
  - Referenziert auf Keycloak ID

## 5. DTOs (`com.simplesalesman.dto`)
- `AddressDto`
- `ProjectDto`
- `NoteDto`
- `ImportResultDto`
- `StatusUpdateDto`
- `UserDto`

## 6. Mapper (`com.simplesalesman.mapper`)
- Implementiert mit MapStruct oder manuell
- `AddressMapper`
- `ProjectMapper`
- `NoteMapper`

## 7. Config & Util (`com.simplesalesman.config`, `com.simplesalesman.util`)
- `SecurityConfig`
  - Spring Security + Keycloak Adapter
- `KeycloakConfig`
  - Konfiguration für OAuth2 Code Flow
- `AsyncConfig`
  - ThreadPoolTaskExecutor
- `ExcelUtil`
  - Apache POI Helper
- `EncryptionUtil`
  - AES-Verschlüsselung für DSGVO-Daten
- `WeatherClient`
  - REST-Client für Wetter-API (Java HTTPClient oder RestTemplate)

## 8. Exception Handling (`com.simplesalesman.exception`)
- `GlobalExceptionHandler` mit `@ControllerAdvice`
- Eigene Exceptions: `ExcelImportException`, `DataValidationException`

## 9. Tests (`src/test/java/com/simplesalesman`)
- `AddressServiceTest`
- `ExcelImportServiceTest`
- `NoteControllerTest`
- `SecurityIntegrationTest`
- `WeatherServiceMockTest`


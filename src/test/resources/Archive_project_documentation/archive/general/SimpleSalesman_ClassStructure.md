
# Simple Salesman – Klassendiagramm (Strukturvorschlag)

## 1. Controller-Schicht (`com.simplesalesman.controller`)
- `AddressController`
  - GET /api/v1/addresses
  - POST /api/v1/addresses/import
- `ProjectController`
  - GET /api/v1/projects/{id}
  - PUT /api/v1/projects/{id}
- `NoteController`
  - POST /api/v1/addresses/{id}/notes
- `AuthController` *(optional, für User-Infos)*

## 2. Service-Schicht (`com.simplesalesman.service`)
- `AddressService`
  - Enthält Logik zur Adressverarbeitung, Import, Matching
- `ProjectService`
  - Handhabt Projektdaten, Statusaktualisierung
- `NoteService`
  - Bearbeitet Notizen, Speichern, Validierung
- `ImportService`
  - Asynchrone Verarbeitung der Excel-Dateien
- `UserService` *(optional, für Benutzer-Infos)*

## 3. Repository-Schicht (`com.simplesalesman.repo`)
- `AddressRepository extends JpaRepository<Address, Long>`
- `ProjectRepository extends JpaRepository<Project, Long>`
- `NoteRepository extends JpaRepository<Note, Long>`
- `RegionRepository extends JpaRepository<Region, Long>`
- `StatusRepository extends JpaRepository<Status, Long>`
- `UserRepository` *(optional bei User-Verwaltung)*

## 4. Entity-Schicht (`com.simplesalesman.entity`)
- `Address`
  - id, street, postalCode, city, Region region
- `Project`
  - id, name, Status status, String operator, String constructionCompany
- `Note`
  - id, content, LocalDateTime createdAt, Address address
- `Region`
  - id, name
- `Status`
  - id, label
- `User` *(optional)*
  - id, username, role

## 5. DTOs (`com.simplesalesman.dto`)
- `AddressDto`
- `ProjectDto`
- `NoteDto`
- `ImportResultDto`
- `StatusUpdateDto`
- `UserDto` *(optional)*

## 6. Mapper (`com.simplesalesman.mapper`)
- `AddressMapper`
- `ProjectMapper`
- `NoteMapper`
- `UserMapper` *(optional)*

## 7. Util & Config (`com.simplesalesman.util`, `com.simplesalesman.config`)
- `ExcelReaderUtil`
- `SecurityConfig`
- `AsyncConfig`
- `LoggingAspect` *(optional)*
- `EncryptionUtil` *(für DSGVO-Verschlüsselung)*

## 8. Tests (`src/test/java/...`)
- `AddressServiceTest`
- `ProjectControllerTest`
- `ImportServiceIntegrationTest`
- `NoteRepositoryTest`

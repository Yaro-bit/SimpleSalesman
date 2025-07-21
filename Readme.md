# Simple Salesman

![version](https://img.shields.io/badge/version-0.0.93-blue)

Backend prototype for a local web application designed for efficient management and documentation of address data, notes, and project status in door-to-door sales. The application is lightweight, fully local, privacy-compliant, and optimized for small teams.

---

## Version

**Current Version:** 0.0.93 
**Last Update:** 2025-07-21

---

## Known Issues

- **Severe Performance Issues on Search :**
---

## Changelog

### Version 0.0.93
- Performance changes - critical bug fixing
- Address entity optimized for Hibernate performance: 
 replaced List<Note> and List<Project> with Set to resolve 
 MultipleBagFetchException and improve eager fetch efficiency.

- AddressRepository.findAllWithNotesProjectsAndRegion() 
 added with optimized JOIN FETCH strategy for loading associated 
 entities in a single query.

Mapping and DTO handling adjusted to accommodate Set semantics without duplicate overhead.
### Version 0.0.92
- Document refactoring

### Version 0.0.911
- Full GUI refactor with responsive, professional design.
- Weather-related fixes on the frontend, now properly retrieving weather data based on the browser's location.
- Complete refactor of backend weather handling for improved accuracy and performance.
- Refactored `index.html` to align with the updated weather data processing logic.

### Version 0.0.81
- Weather implementation refactoring.
- Added documentation for every service.
- Created simple fallback for weather data retrieval.

### Version 0.0.8
- Interactive API GUI update: Enhanced Thymeleaf templates for browser-based API testing.
- Refined and improved design on the GUI (visual polish, usability, and responsiveness).

### Version 0.0.7
- Interactive API GUI - Thymeleaf template for browser-based API testing.
- OAuth2 Authentication: Secure Keycloak login flow with PKCE (eliminates CORS issues).
- Live API Testing: User-friendly interface for all REST endpoints with real-time response display.
- Token Management: Automatic JWT handling, expiry detection, and secure logout.
- Configuration Display: Real-time OAuth settings and user information panel.
- Responsive Design: Modern gradient UI with mobile-friendly layout.
- Production Ready: Thymeleaf integration for deployment flexibility across environments.

### Version 0.0.6
- Translated to English.
- Complete GDPR and EU AI Act documentation added (`README.md`).
- New and revised DTOs (`UserDto`, `ProjectDto`, `NoteDto`, `StatusUpdateDto`, `ImportResultDto`, `AddressDto`):
  - Validation with `@NotBlank`, `@Size`, `@DecimalMin`, `@PositiveOrZero`.
  - Unified logging in all setters (`LoggerFactory`).
  - Swagger/OpenAPI comments with `@Schema` added.
  - Utility methods `toString()`, `equals()`, `hashCode()` implemented.
- Version numbers & JavaDoc metadata standardized (`@version`, `@since`, author).
- All DTOs brought to production-ready, testable format (null-safe, well-documented).
- Markdown documentation consolidated and structured (`README.md` & `ai-doku.md`).
- Project structure for DTOs and API documentation harmonized.
- Centralized exception handling (global error handler, clearly defined exception classes).
- Encryption utility (`EncryptionUtil`) implemented, key configuration externalized.
- Weather API extended with geolocation support (latitude/longitude endpoint support).
- Frontend (partially) and backend API structure (Swagger/OpenAPI) synchronized.
- Various minor optimizations and refactorings (naming, imports, readability).

### Version 0.0.5
- Weather API implemented (GET `/api/v1/weather?region=...`).
- Refactored all DTO classes: logging, validation, structured comments.
- Test classes revised and adapted to new DTOs.
- New Postman collection for all REST endpoints created.
- Technical preparation for Swagger / OpenAPI.

### Version 0.0.4
- Bugfix: `No qualifying bean of type 'RestTemplate'` resolved by custom bean definition.
- REST API cleaned up and restructured.
- Adapted to Spring Boot 2.4+ (manual `RestTemplate` definition required).

### Version 0.0.3
- Note about changed behavior since Spring Boot 2.4 (no automatic `RestTemplate` bean).
- Bean configuration added.

### Version 0.0.1 - 0.0.2
- Basic testing.
- Pattern creation.

---

## Implemented Features

- **Import of `.xlsx` files** containing address and project information.
- **Management and querying of:**
  - Addresses (CRUD)
  - Projects (CRUD)
  - Notes (CRUD)
- **Mapping between entities and DTOs** with validation and logging.
- **Weather data retrieval** for a selected region.
- Project structure follows **Clean Code** and **Maven standards**.
- **Postman collection** for all endpoints.

### Token-based Processing (Implemented)

**Keycloak JWT**
- **Contents:** roles, user ID, permissions.
- **Storage duration:** session lifetime.
- **Purpose:** authentication and access control.

#### Implemented Components

**Frontend**
- Simple Frontend with Thymeleaf and HTML.
- Responsive design.
- Admin console with access to all APIs.

**Weather Service**
- Description: REST-based queries via wttr.in.
- Token usage: Yes.

---

## Not Included / Planned

- Keycloak is technically integrated but not yet in production.
- GDPR tools (logging, audit trails, permission management) are still missing.
- AI features and automatic text suggestions are planned but not yet implemented.
- Performance upgrade needed.

---

## Used Libraries & Licenses

- **Apache POI** (Apache License 2.0): Excel import.
- **Spring Boot** (Apache License 2.0): REST backend.
- **Keycloak** (Apache License 2.0): Authentication.
- **MapStruct** (Apache License 2.0): DTO mapping.
- **Bootstrap** (MIT License): Icons and Frontend tools.

---

## Design Patterns

| **Pattern**            | **Type**      | **Used in**                          |
|------------------------|---------------|--------------------------------------|
| **MVC**                | Architecture  | Spring Controller, Thymeleaf, DTOs   |
| **DTO**                | Structure     | NoteDto, AddressDto, etc.            |
| **Service Layer**      | Architecture  | All *Service classes                 |
| **Repository**         | Structure     | All *Repository interfaces           |
| **Mapper / Adapter**   | Structure     | *Mapper classes                      |
| **Singleton**          | Creation      | Spring Beans                         |
| **Factory**            | Creation      | Spring Bean creation                 |
| **Builder**            | Creation      | DTO/Entity creation                  |
| **Strategy (optional)**| Behavior      | Potential for Excel, weather strat.  |
| **Observer (JS)**      | Behavior      | Event-based UI logic                 |
| **(Thymeleaf)**        | Behavior      | layout.html Template layout          |

---

## Prerequisites

- Java 17
- Maven
- PostgreSQL
- Optional: Keycloak

---

## Clone the Project

```bash
git clone https://github.com/Yaro-bit/SimpleSalesman.git

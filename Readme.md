# Simple Salesman

![version](https://img.shields.io/badge/version-0.0.9-blue)

Backend prototype for a local web application designed for efficient management and documentation of address data, notes, and project status in door-to-door sales. The application is lightweight, fully local, privacy-compliant, and optimized for small teams.

---

## Version

**Current:** 0.0.9  
**Last Update:** 2025-06-30

## Known Issues:
    Severe performance issues on large imports:
    Importing 70,000 address entries currently takes approximately 10,498,144 ms (≈ 2 hours, 54 minutes, 58 seconds). The same problem occurs when selecting all addresses: the browser attempts to load the entire address dataset at once, resulting in extremely poor performance and a nearly unusable frontend for large datasets.
    Cause: Both import and retrieval routines lack batching, streaming, or pagination, leading to excessive memory and processing requirements.
    Workaround: None at this time; consider limiting import/export size or adding pagination.
	


### Changes in Version 0.0.911
Full GUI refactor with responsive, professional design.
Weather-related fixes on the frontend, now properly retrieving weather data based on the browser's location.
Complete refactor of backend weather handling for improved accuracy and performance.
Refactored `index.html` to align with the updated weather data processing logic.


### Changes in Version 0.0.81
Weather Implementattion refactoring, documentation on every service. Creation of simple fallback.
	
### Changes in Version 0.0.8
Interactive API GUI update: Enhanced Thymeleaf templates for browser-based API testing
Refined and improved design on GUI (visual polish, usability, and responsiveness)


### Changes in Version 0.0.7

Interactive API GUI -  Thymeleaf template for browser-based API testing
OAuth2 Authentication: Secure Keycloak login flow with PKCE (eliminates CORS issues)
Live API Testing: User-friendly interface for all REST endpoints with real-time response display
Token Management: Automatic JWT handling, expiry detection, and secure logout
Configuration Display: Real-time OAuth settings and user information panel
Responsive Design: Modern gradient UI with mobile-friendly layout
Production Ready: Thymeleaf integration for deployment flexibility across environments



### Changes in Version 0.0.6

- Translation to English
- Complete GDPR and EU AI Act documentation added (`README.md`)
- New and revised DTOs (`UserDto`, `ProjectDto`, `NoteDto`, `StatusUpdateDto`, `ImportResultDto`, `AddressDto`)
  - Validation with `@NotBlank`, `@Size`, `@DecimalMin`, `@PositiveOrZero`
  - Unified logging in all setters (`LoggerFactory`)
  - Swagger/OpenAPI comments with `@Schema` added
  - Utility methods `toString()`, `equals()`, `hashCode()` implemented
- Version numbers & JavaDoc metadata standardized (`@version`, `@since`, author)
- All DTOs brought to production-ready, testable format (null-safe, well-documented)
- Markdown documentation consolidated and structured (`README.md` & `ai-doku.md`)
- Project structure for DTOs and API documentation harmonized
- All repositories, services, and mappers provided with consistent JavaDoc comments
- Centralized exception handling (global error handler, clearly defined exception classes)
- Encryption utility (`EncryptionUtil`) implemented, key configuration externalized
- Weather API extended with geolocation support (latitude/longitude endpoint support)
- Frontend (partially) and backend API structure (Swagger/OpenAPI) synchronized
- Various minor optimizations and refactorings (naming, imports, readability)

### Changes in Version 0.0.5

- Weather API implemented (GET `/api/v1/weather?region=...`)
- Refactored all DTO classes: logging, validation, structured comments
- Test classes revised and adapted to new DTOs
- New Postman collection for all REST endpoints created
- Technical preparation for Swagger / OpenAPI

### Changes in Version 0.0.4

- Bugfix: `No qualifying bean of type 'RestTemplate'` resolved by custom bean definition
- REST API cleaned up and restructured
- Adapted to Spring Boot 2.4+ (manual `RestTemplate` definition required)

### Changes in Version 0.0.3

- Note about changed behavior since Spring Boot 2.4 (no automatic `RestTemplate` bean)
- Bean configuration added

### Changes in Version 0.0.1 - 0.0.2

- Basic testing
- Pattern creation

---

## Implemented Features
- Import of `.xlsx` files containing address and project information
- Management and querying of:
  - Addresses (CRUD)
  - Projects (CRUD)
  - Notes (CRUD)
- Mapping between entities and DTOs with validation and logging
- Weather data retrieval for a selected region
- Project structure follows Clean Code and Maven standards
- Postman collection for all endpoints

### Token-based Processing (Implemented)

**Keycloak JWT**  
- Contents: roles, user ID, permissions  
- Storage duration: session lifetime  
- Purpose: authentication and access control  

#### Implemented Components

**Frontend**
- Simple Frontend with Thymeleaf and HTML
- Responsive design
- Admin console with all API's

**Weather Service**  
- Description: REST-based queries via wttr.in  
- Token usage: Yes  

## Not Included / Planned
- Keycloak is technically integrated but not yet in production
- GDPR tools (logging, audit trails, permission management) are still missing
- AI features and automatic text suggestions are planned but not yet implemented
- Performance upgrade

## Used Libraries & Licenses

- Apache POI  (Apache License 2.0): Excel import  
- Spring Boot  (Apache License 2.0): REST backend  
- Keycloak   (Apache License 2.0): Authentication  
- MapStruct  (Apache License 2.0): DTO mapping  

---

## Getting Started (Backend Only)

### Prerequisites

- Java 17  
- Maven  
- PostgreSQL  
- Optional: Keycloak  

### Clone the Project

```bash
git clone https://github.com/Yaro-bit/SimpleSalesman.git

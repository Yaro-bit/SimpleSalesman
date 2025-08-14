# Simple Salesman – Project History

> **Note:** This project was originally developed as a **school assignment** during a WIFI course.  
> The goal was to build a backend prototype for address and note management with Excel import, optimized for small sales teams.  
> However, the chosen technology stack turned out to be **not well-suited** for the intended practical use.

---

## Why It Was Reworked
Simple Salesman was a **complex software** that required mandatory hosting.  
For small sales teams, this resulted in ongoing **hosting and operational costs** that were **not proportional** to the actual usage and benefit.

---

## Transformation into Address Manager Pro
Due to these limitations, the project was **fully rebuilt** into **[Address Manager Pro](../AddressManagerPro)**:

- **Lighter, more flexible, and easier to deploy** (Next.js + React + TypeScript instead of a complex Spring Boot setup)  
- **No mandatory hosting** – can run locally or optionally online  
- **Core features focus**: Excel import, deduplication, search, filter, grouping, inline notes, and CSV export  
- **Improved UX** with modern UI (Tailwind CSS)  
- **Greater adaptability** without being tied to a hosted backend  

---

**Status:** Simple Salesman is **no longer actively developed**.  
All future improvements and features will go directly into Address Manager Pro.

---

## Design Patterns

| **Pattern**            | **Type**      | **Used in**                          |
|------------------------|---------------|---------------------------------------|
| **MVC**                | Architecture  | Spring Controller, Thymeleaf, DTOs   |
| **DTO**                | Structure     | NoteDto, AddressDto, etc.            |
| **Service Layer**      | Architecture  | All *Service classes                  |
| **Repository**         | Structure     | All *Repository interfaces            |
| **Mapper / Adapter**   | Structure     | *Mapper classes                       |
| **Singleton**          | Creation      | Spring Beans                          |
| **Factory**            | Creation      | Spring Bean creation                  |
| **Builder**            | Creation      | DTO/Entity creation                   |
| **Strategy (optional)**| Behavior      | Potential for Excel, weather strat.   |
| **Observer (JS)**      | Behavior      | Event-based UI logic                  |
| **(Thymeleaf)**        | Behavior      | `layout.html` Template layout         |

---

## Prerequisites
- Java 17  
- Maven  
- PostgreSQL  
- Optional: Keycloak  

---

## Changelog

### Version 0.0.94
- Performance improvements on Note and Import

### Version 0.0.93
- Performance changes - critical bug fixing
- Address entity optimized for Hibernate performance: replaced `List<Note>` and `List<Project>` with `Set` to resolve MultipleBagFetchException and improve eager fetch efficiency
- `AddressRepository.findAllWithNotesProjectsAndRegion()` added with optimized JOIN FETCH strategy for loading associated entities in a single query
- Mapping and DTO handling adjusted to accommodate `Set` semantics without duplicate overhead

### Version 0.0.92
- Document refactoring

### Version 0.0.911
- Full GUI refactor with responsive, professional design
- Weather-related fixes on the frontend, now properly retrieving weather data based on the browser's location
- Complete refactor of backend weather handling for improved accuracy and performance
- Refactored `index.html` to align with the updated weather data processing logic

### Version 0.0.81
- Weather implementation refactoring
- Added documentation for every service
- Created simple fallback for weather data retrieval

### Version 0.0.8
- Interactive API GUI update: Enhanced Thymeleaf templates for browser-based API testing
- Refined and improved design on the GUI (visual polish, usability, and responsiveness)

### Version 0.0.7
- Interactive API GUI – Thymeleaf template for browser-based API testing
- OAuth2 Authentication: Secure Keycloak login flow with PKCE (eliminates CORS issues)
- Live API Testing: User-friendly interface for all REST endpoints with real-time response display
- Token Management: Automatic JWT handling, expiry detection, and secure logout
- Configuration Display: Real-time OAuth settings and user information panel
- Responsive Design: Modern gradient UI with mobile-friendly layout
- Production Ready: Thymeleaf integration for deployment flexibility across environments

### Version 0.0.6
- Translated to English
- Complete GDPR and EU AI Act documentation added (`README.md`)
- New and revised DTOs with validation, logging, and Swagger/OpenAPI annotations
- Centralized exception handling and encryption utility implemented
- Weather API extended with geolocation support
- Minor optimizations and refactorings

### Version 0.0.5
- Weather API implemented
- Refactored all DTO classes
- Created Postman collection for all REST endpoints
- Technical preparation for Swagger/OpenAPI

### Version 0.0.4
- Fixed `No qualifying bean of type 'RestTemplate'` error
- REST API cleaned up and restructured
- Adapted to Spring Boot 2.4+

### Version 0.0.3
- Added Bean configuration for `RestTemplate`

### Version 0.0.1 – 0.0.2
- Basic testing and pattern creation

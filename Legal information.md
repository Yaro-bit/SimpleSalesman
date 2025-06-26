# Legal & Technical Documentation – Simple Salesman

## Project Overview

**Application Name:**  
Simple Salesman

**Version:**  
See `README.md` – Current: 0.0.81 (Last Update: 2025-06-26)

**Purpose:**  
Efficient management and documentation of addresses, notes, and project status in door-to-door sales.

**Maintainer:**  
Yaroslav Vol. (Private individual, open source initiative)

**Technology Stack:**  
Spring Boot · PostgreSQL · Apache POI · Keycloak · HTML/CSS/JavaScript (Thymeleaf)

---

## AI Feature Documentation (EU AI Act)

> ℹ️ No GPT models are currently active or embedded. Planned AI is local-only, optional, and fully transparent.

### Implemented

**Weather Service**  
- REST-based service via `wttr.in`  
- No local model / external processing only  
- No tokenization, AI, or tracking  

### Planned

- AI text suggestions for note-taking  
- No automation or decision-making by AI  
- Local GPT-2 model (planned)  
- Optional via frontend toggle (opt-out functionality)

---

## Token-Based Processing

**Keycloak JWT**  
- Contains: Roles, User ID, Permissions  
- Stored: Session duration  
- Purpose: Auth & access control

---

## Data Protection & GDPR Compliance

| Code | Feature                  | Status                        |
|------|--------------------------|-------------------------------|
| DS1  | Logging                  | Enabled (Spring Boot logging) |
| DS2  | Encryption               | Database-level (PostgreSQL)   |
| DS3  | Access Control           | Keycloak-based                |
| DS4  | Deletion Function        | Basic structure implemented   |
| DS5  | Consent Management       | Manual, planned UI frontend   |

---

## EU AI Act Compliance Status

| Code | Requirement                          | Status         |
|------|--------------------------------------|----------------|
| KI1  | No active AI = Exempt                | ✅ Fulfilled    |
| KI2  | Opt-out mechanism (frontend)         | ⚠️ In planning  |
| KI3  | No data processing by AI             | ✅ Fulfilled    |
| KI4  | Documentation for local model        | 📝 Planned      |

---

## Licenses & Libraries

- **Apache POI** – Excel Import (Apache License 2.0)  
- **Spring Boot** – REST Backend (Apache License 2.0)  
- **Keycloak** – Authentication (Apache License 2.0)  
- **MapStruct** – DTO Mapping (Apache License 2.0)  

---

## Changelog Summary (last updated: 0.0.81)

- Weather API refactored (fallback support added)  
- JWT login + API access flow via Keycloak  
- Excel Import via Apache POI  
- Frontend enhancements with Thymeleaf  
- Full GDPR/AI documentation added  
- DTOs and error handling refactored and documented  

---

## Development Roadmap

**Implemented:**  
- REST API for address, note, and project management  
- Excel import/export  
- Weather integration  
- Keycloak JWT Auth  
- Interactive API GUI (Thymeleaf-based)  
- GitHub CI/CD  

**Planned:**  
- AI-based suggestions (optional)  
- Local GPT-2 note support  
- Admin UI (logs, backups)  
- Enhanced UI: avatar upload, dark mode  
- Daily Excel export  
- Easter eggs, progress indicators  

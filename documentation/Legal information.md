# Legal & Technical Documentation – Simple Salesman

## Project Overview

**Application Name:**  
Simple Salesman

**Version:**  
See `README.md` – Current: 0.0.9 (Last Update: 2025-07-02)

**Purpose:**  
Efficient management and documentation of addresses, notes, and project status in door-to-door sales.

**Maintainer:**  
Yaroslav Vol. (Private individual, open source initiative)

**Technology Stack:**  
Spring Boot · PostgreSQL · Apache POI · Keycloak · Bootstrap 5.3+ · HTML/CSS/JavaScript (Thymeleaf)

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
- **Contains:** Roles, User ID, Permissions
- **Stored:** Session duration
- **Purpose:** Auth & access control

---

## Data Protection & GDPR Compliance

| Code  | Feature               | Status                           |
|-------|-----------------------|----------------------------------|
| DS1   | Logging               | Enabled (Spring Boot logging)    |
| DS2   | Encryption            | Database-level (PostgreSQL)      |
| DS3   | Access Control        | Keycloak-based                   |
| DS4   | Deletion Function     | Basic structure implemented      |
| DS5   | Consent Management    | Manual, planned UI frontend      |

---

## EU AI Act Compliance Status

| Code  | Requirement                       | Status          |
|-------|------------------------------------|-----------------|
| KI1   | No active AI = Exempt             | ✅ Fulfilled    |
| KI2   | Opt-out mechanism (frontend)      | ⚠️ In planning  |
| KI3   | No data processing by AI          | ✅ Fulfilled    |
| KI4   | Documentation for local model     | 📝 Planned      |

---

## Licenses & Libraries

- **Apache POI** – Excel Import (Apache License 2.0)
- **Spring Boot** – REST Backend (Apache License 2.0)
- **Keycloak** – Authentication (Apache License 2.0)
- **MapStruct** – DTO Mapping (Apache License 2.0)
- **Bootstrap 5.3.2** – UI Framework (MIT License)
- **Bootstrap Icons 1.11.0** – Icon Library (MIT License)

---

## Changelog Summary (last updated: 0.0.9)

- **[0.0.9]** UI completely refactored to Bootstrap 5.3+ framework
  - Replaced custom CSS with Bootstrap components
  - Added responsive mobile navigation with collapsible sidebar
  - Integrated Bootstrap Icons for consistent iconography
  - Enhanced tables with Bootstrap styling and pagination
  - Improved modal handling with native Bootstrap modals
  - Added loading spinners and alert components
  - Maintained full backwards compatibility with existing API
- **[0.0.81]** Weather API refactored (fallback support added)
- JWT login + API access flow via Keycloak
- Excel Import via Apache POI
- Frontend enhancements with Thymeleaf
- Full GDPR/AI documentation added
- DTOs and error handling refactored and documented

---

## UI Framework Documentation

**Bootstrap Integration (v0.0.9+)**
- **Framework:** Bootstrap 5.3.2 (CDN-based)
- **Icons:** Bootstrap Icons 1.11.0
- **No jQuery dependency**
- **CDN Requirements:** Active internet connection required for Bootstrap resources
- **Browser Compatibility:** Chrome 60+, Firefox 60+, Safari 12+, Edge 79+
- **Accessibility:** WCAG 2.1 AA compliant through Bootstrap components

---

## Development Roadmap

**Implemented:**
- REST API for address, note, and project management
- Excel import/export
- Weather integration
- Keycloak JWT Auth
- Interactive API GUI (Thymeleaf-based)
- GitHub CI/CD
- **Bootstrap 5.3+ UI framework**
- **Responsive mobile design with collapsible navigation**

**Planned:**
- Documentation refactoring
- Performance updates
- AI-based suggestions (optional)
- Local GPT-2 note support
- Admin UI (logs, backups)
- Enhanced UI: avatar upload, ~~dark mode~~
- Easter eggs, progress indicators


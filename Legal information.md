## Project Overview

**Application Name:**  
Simple Salesman -  see readme for Version

**Purpose:**  
Management of addresses, notes, and project status for door-to-door sales  

**Responsible:**  
Yaroslav Vol. (Private individual, Open Source)  

**Technology Stack:**  
Spring Boot, PostgreSQL, Apache POI, Keycloak, HTML/JavaScript  

---

## Documentation of AI Features According to the EU AI Act

>Note: The GPT-2-based text editor was considered during planning but has not been implemented yet. No GPT models are present in the code.

### Actually Implemented Components

**Weather Service**  
- Description: REST-based query via wttr.in  
- Local processing: No (external)  
- Model version: –  
- Token usage: No  

---

## Purpose of Planned AI Usage (Future)

- Text suggestions while writing notes  
- No evaluation or automation of decisions  
- Locally hosted model (planned: GPT-2)  
- AI option can be disabled (opt-out planned in frontend)  

---

## Token-Based Processing (Implemented)

**Keycloak JWT**  
- Content: Roles, user ID, permissions  
- Storage duration: Session lifetime  
- Purpose: Authentication and access control  

---

## Data Protection & Security (GDPR)

**DS1 – Logging**  
Activated via Spring Boot logging  

**DS2 – Encryption**  
Data encryption (PostgreSQL)  

**DS3 – Access Control**  
Implemented via Keycloak  

**DS4 – Deletion Function**  
Partially available, API base structure implemented  

**DS5 – Consent**  
Frontend integration planned, privacy policy currently manual  

---

## EU AI Act Compliance (Current State)

**KI1**  
No active AI functions present → not required  

**KI2**  
Frontend opt-out planned → in preparation  

**KI3**  
No AI data processing → fulfilled  

**KI4**  
Model documentation prepared but not yet implemented → planned  

---

## Used Libraries & Licenses

- Apache POI  (Apache License 2.0): Excel import  
- Spring Boot  (Apache License 2.0): REST backend  
- Keycloak   (Apache License 2.0): Authentication  
- MapStruct  (Apache License 2.0): DTO mapping  

---

## Change Log (as of Version 0.6)

- Weather API integrated via wttr.in (`WeatherClient`)  
- Excel import service implemented using Apache POI  
- Postman tests for API prepared  
- Keycloak integration for authentication (JWT)  
- Notes API implemented  

---

## Development Status

### Already Implemented

- REST API (CRUD for addresses and notes)  
- Excel import via controller  
- Weather service integration  
- Keycloak login with token handling in frontend  
- CI/CD pipeline and GitHub deployment  

### Planned

**Frontend:**  
- Responsive design  
- Filtering functions  
- Dark Mode  
- Data table  
- Avatar upload  
- Weather display via geolocation  

**Backend:**  
- Local text editor  
- Local GPT-2 access  
- Admin area with backup and log functions  
- Gamification elements (e.g. Easter eggs, progress tracking)  
- Backup, daily Excel export  

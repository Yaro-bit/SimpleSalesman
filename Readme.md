# Simple Salesman

![version](https://img.shields.io/badge/version-0.0.5-blue)

Backend-Prototyp für eine lokale Webanwendung zur effizienten Verwaltung und Dokumentation von Adressdaten, Notizen und Auftragsstatus im Door-to-Door-Vertrieb. Die Anwendung ist schlank, vollständig lokal, datenschutzkonform und optimiert für kleine Teams.

---

## Version

**Aktuell:** 0.0.5  
**Letztes Update:** 16.06.2025

### Änderungen in Version 0.0.5

- Wetter-API implementiert (GET `/api/v1/weather?region=...`)
- Refactoring aller DTO-Klassen: Logging, Validierung, strukturierte Kommentare
- Testklassen überarbeitet und an neue DTOs angepasst
- Neue Postman-Collection für alle REST-Endpunkte erstellt
- Technische Vorbereitung für Swagger / OpenAPI

### Änderungen in Version 0.0.4

- Fehlerbehebung: `No qualifying bean of type 'RestTemplate'` durch eigene Bean-Definition
- REST API bereinigt und strukturiert
- Anpassung an Spring Boot 2.4+ (manuelle `RestTemplate`-Definition erforderlich)

### Änderungen in Version 0.0.3

- Hinweis auf verändertes Verhalten ab Spring Boot 2.4 (kein `RestTemplate` mehr automatisch enthalten)
- Bean-Konfiguration ergänzt

---

## Aktueller Entwicklungsstand (Version 0.0.5)

- Backend-Prototyp ist lauffähig und getestet
- Excel-Import von Adress- und Projektdaten ist implementiert
- Alle Entitäten, DTOs, Mapper, Repositories und Services sind erstellt
- Unit-Tests für Geschäftslogik, DTOs und Importmechanismen vorhanden
- Wetterabfrage über externe REST API (`wttr.in`) implementiert
- Postman-Collection zur API-Nutzung liegt im Projektverzeichnis vor
- Keycloak-Anbindung ist vorbereitet (noch nicht aktiviert)
- Es existiert aktuell kein Web-Frontend

---

## Implementierte Funktionen

- Import von `.xlsx`-Dateien mit Adress- und Projektinformationen
- Bearbeitung und Abfrage von:
  - Adressen
  - Projekten inkl. Statusänderung
  - Notizen (CRUD)
- Mapping zwischen Entitäten und DTOs mit Validierung und Logging
- Wetterabfrage für eine gewählte Region
- Projektstruktur nach Clean-Code- und Maven-Standards
- Postman-Testsammlung für alle Endpunkte

---

## Nicht enthalten / in Planung

- Keine Benutzeroberfläche (UI) zur Bedienung der API
- Keycloak ist technisch eingebunden, aber noch nicht in Produktion
- DSGVO-Tools (Logging, Protokollierung, Rechteverwaltung) fehlen noch
- Keine Admin-Konsole oder Benutzerübersicht
- KI-Funktionen und automatische Textvorschläge sind geplant, aber noch nicht umgesetzt

---

## Einstieg (nur Backend)

### Voraussetzungen

- Java 17
- Maven
- PostgreSQL
- Optional: Keycloak

### Projekt klonen

```bash
git clone https://github.com/Yaro-bit/SimpleSalesman.git

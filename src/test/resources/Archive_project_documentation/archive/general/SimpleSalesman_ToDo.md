# Simple Salesman Projekt – ToDo-Liste für Junior

## 1. Projektstruktur & Architektur
- **Entscheidung**: Monolithische, schichtorientierte Architektur.
- **Schichten anlegen**:
  - API (Spring Boot)
  - Business Logic
  - Datenzugriff (Repository)
- **Datenbankschema erstellen**:
  - ER-Modell mit Region, Adresse, Projekt, Status
  - Indizes für häufige Abfragen

## 2. Backend-Entwicklung
- **REST-API**:
  - Endpunkte für Adressen, Projekte, Notizen
  - Eingabevalidierung (@Valid, @NotNull)
- **Excel-Import**:
  - Apache POI einbinden
  - Asynchrone Verarbeitung (z. B. Spring @Async oder Message Queue)
- **Geschäftslogik** implementieren:
  - Adressverwaltung, Status-Updates

## 3. Sicherheit & Authentifizierung
- **Keycloak** einrichten und integrieren (OAuth2 Authorization Code Flow)
- **Rollen & Berechtigungen** (Verkäufer, Admin) mit Spring Security
- **HTTPS** erzwingen
- **Input-Sanitation** gegen Injection-Angriffe

## 4. Frontend-Entwicklung
- **HTML/CSS** Basisstruktur (kein Framework)
- **JavaScript** für:
  - Datenanzeige (Fetch API)
  - Excel-Upload
  - Notizbearbeitung
- **Responsive Design** (Media Queries)

## 5. Testing
- **Unit-Tests** mit JUnit (Business-Logik, Import)
- **Integrationstests** mit Spring Boot Test / Testcontainers
- **End-to-End-Tests** mit Cypress

## 6. CI/CD & Deployment
- **GitHub Actions** Pipeline:
  1. Build
  2. Tests
  3. Docker-Image bauen
  4. Deployment
- **Blue/Green-Deployment** oder Canary Releases einrichten

## 7. Datenschutz & DSGVO
- **Verschlüsselung** personenbezogener Daten (AES)
- **Recht auf Vergessenwerden** implementieren
- **Zugriffprotokollierung** aller Änderungen

## 8. Monitoring & Logging
- **Prometheus** + **Grafana** einrichten (Metriken, Health-Endpoints)
- **ELK Stack** für strukturierte Logs
- **Error Tracking** mit Sentry

## 9. Dokumentation
- **Swagger/OpenAPI** für API-Dokumentation
- **Architektur-Diagramme** (draw.io / Lucidchart)
- **Benutzerhandbuch** und **Entwickler-Guide**

## 10. Feedback & Iteration
- **Regelmäßiges Nutzer-Feedback** sammeln
- **Refactoring** basierend auf Feedback
- **Regelmäßige Retrospektiven** im Team

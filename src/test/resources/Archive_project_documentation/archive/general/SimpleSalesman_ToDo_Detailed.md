# Simple Salesman Projekt – ToDo-Liste für Junior (detailliert)

## 1. Projektstruktur & Architektur
- **Entscheidung: Monolithische, schichtorientierte Architektur**
  - Begründung: Einfacheres Deployment, geringere Komplexität für kleinere Teams.
- **Schichten anlegen**
  1. **API-Schicht (Spring Boot)**  
     - REST-Controller in `com.simplesalesman.controller`  
     - Endpunkt-URLs konsistent im Stil `/api/v1/...`
  2. **Business Logic**
     - Services in `com.simplesalesman.service`  
     - Geschäftsregeln hier kapseln, z.B. Validierungs- oder Matching-Logik.
  3. **Datenzugriff (Repository)**
     - Spring Data JPA Repositories in `com.simplesalesman.repo`  
     - Nutzen von `@Repository`-Annotation und JPA-Methoden.
- **Datenbankschema erstellen**
  - **ER-Modell**
    - **Region** (id, name)  
    - **Adresse** (id, street, postalCode, city, region_id FK)  
    - **Projekt** (id, name, status_id FK, operator, constructionCompany)  
    - **Status** (id, label)
  - **Indizes**
    - B-Tree-Index auf `adresse.postalCode` und `projekt.status_id` für schnelle Filter.
  - **Migrations-Tool**: Flyway oder Liquibase konfigurieren.

## 2. Backend-Entwicklung
- **REST-API**
  - **Endpunkte**:  
    - `GET /api/v1/addresses`  
    - `POST /api/v1/addresses/import`  
    - `GET /api/v1/projects/{id}`  
    - `POST /api/v1/addresses/{id}/notes`
  - **Eingabevalidierung**
    - DTOs mit `@Valid`, Felder mit `@NotNull`, `@Size`, `@Pattern`.
    - Fehlermapper (`@ControllerAdvice`) für einheitliches Error-Format.
- **Excel-Import**
  - **Apache POI**: Utility-Klasse `ExcelReader` für Mapping auf DTOs.
  - **Asynchrone Verarbeitung**:  
    - Annotiere Methoden mit `@Async` oder verwende Spring Boot TaskExecutor.  
    - Rückgabe eines Job-Status-IDs für den Upload.
- **Geschäftslogik**
  - **Adressverwaltung**:  
    - Prüfung auf Duplikate via Unique-Constraints (z. B. Kombination `street+postalCode`).
  - **Status-Updates**:  
    - Validierungs-Regeln (z. B. nur vorherige Stati erlauben).

## 3. Sicherheit & Authentifizierung
- **Keycloak**
  - Installation in Docker-Container, Realm-Konfiguration für Clients und Rollen.
  - Spring Boot Adapter konfigurieren (`application.properties`).
- **Rollen & Berechtigungen**
  - Rollen: `ROLE_SALES`, `ROLE_ADMIN`.
  - Methoden-Security mit `@PreAuthorize("hasRole('ADMIN')")`.
- **HTTPS erzwingen**
  - Konfiguration im Spring Boot: `server.ssl.key-store`, Redirect von HTTP auf HTTPS.
- **Input-Sanitation**
  - OWASP Java Encoder für Ausgabe-Encoding.
  - Parameterized Queries oder JPA-Methoden vermeiden SQL-Injection.

## 4. Frontend-Entwicklung
- **HTML/CSS Basisstruktur**
  - Semantic HTML5 (z. B. `<header>`, `<main>`, `<footer>`).
  - BEM-Namenskonvention für CSS-Klassen.
- **JavaScript**
  - **Fetch API**: asynchrone Requests mit `async/await`.
  - **Modulare Struktur**: Funktionen in ES6-Module (`import/export`).
- **Excel-Upload**
  - `<input type="file">`, Drag-and-Drop optional.
  - Fortschrittsanzeige mit `XMLHttpRequest` Events.
- **Notizbearbeitung**
  - WYSIWYG-Editor (z. B. TinyMCE leichtgewichtig) oder `contenteditable`.
- **Responsive Design**
  - Mobile-First-Ansatz.
  - Breakpoints definieren (320px, 768px, 1024px).

## 5. Testing
- **Unit-Tests (JUnit 5)**
  - Test Coverage Ziel: >= 80 %.
  - Mocking mit Mockito für Service- und Repo-Layer.
- **Integrationstests**
  - Spring Boot Test mit in-memory DB (H2) oder Testcontainers für PostgreSQL.
  - End-to-End-API-Tests mit MockMVC.
- **End-to-End-Tests (Cypress)**
  - Testfälle für Upload, Anzeige, Filter, Notizen.
  - CI-Integration, Screenshots auf Fehler.

## 6. CI/CD & Deployment
- **GitHub Actions**
  1. **Checkout Code**
  2. **Build & Test** (`mvn clean verify`)
  3. **Docker Build**  
  4. **Push Image** zu Docker Registry  
  5. **Deploy** auf Kubernetes/VM via `kubectl` oder SSH-Skript.
- **Deployment-Strategie**
  - **Blue/Green**: zwei Umgebungen, Traffic-Switch via Load-Balancer.
  - **Canary**: kleine Prozentzahl Nutzer auf neue Version leiten.

## 7. Datenschutz & DSGVO
- **Verschlüsselung**
  - Datenbank: Feldverschlüsselung mit JPA-AttributeConverter (AES256).
  - Transit: TLS 1.2+.
- **Recht auf Vergessenwerden**
  - Soft Delete vs. Hard Delete: Soft Delete markieren, Hard Delete nach Frist.
- **Zugriffsprotokollierung**
  - Audit-Table oder Spring Data Envers für Entity-Historie.

## 8. Monitoring & Logging
- **Prometheus & Grafana**
  - `/actuator/prometheus` Endpoint aktivieren.
  - Dashboards: HTTP-Latenz, Fehlerquote, Heap-Nutzung.
- **ELK Stack**
  - Logstash-Konfiguration, strukturierte JSON-Logs.
  - Kibana-Dashboards für Fehleranalyse.
- **Error Tracking**
  - Sentry: DSN in Config, automatische Exception-Reports.

## 9. Dokumentation
- **Swagger/OpenAPI**
  - `@Operation` und `@Schema` Annotationen.
  - UI: `/swagger-ui.html`.
- **Architektur-Diagramme**
  - draw.io: Komponentendiagramm, Deploymentdiagramm, Sequenzdiagramme.
- **Handbücher**
  - README.md: Projektübersicht, Setup, Konventionen.
  - Contributing.md: Git-Workflow, Code-Style.

## 10. Feedback & Iteration
- **Regelmäßiges Nutzer-Feedback**
  - Monatliche Reviews mit Verkäufern/Admins.
  - Jira/Trello für Tickets und Priorisierung.
- **Refactoring**
  - Code Smells identifizieren (SonarQube).
  - Tech-Debt-Backlog pflegen.
- **Retrospektiven**
  - Nach jedem Sprint: Was lief gut, was nicht?

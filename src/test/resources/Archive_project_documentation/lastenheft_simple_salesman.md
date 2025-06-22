# SimpleSalesman - Projekt-Dokumentation
**Door-to-Door Vertriebsmanagement • Version 0.0.5 • Stand: Juni 2025**

---

## Management Summary

**SimpleSalesman** ist eine lokale Webanwendung zur effizienten Verwaltung von Door-to-Door-Vertriebsaktivitäten. Das System digitalisiert den wöchentlichen Import von Anschlussprojekt-Daten und ermöglicht strukturierte, adress-basierte Kundenbetreuung für kleine Vertriebsteams.

### Geschäftlicher Nutzen
| Kennzahl                  | Wert                                              | Details                                       |           
|---------------------------|---------------------------------------------------|-----------------------------------------------|
| **Zielgruppe**            | Vertrieb/Innenvertrieb (2 Verkäufer + 1 Admin)    | Door-to-Door Breitband-Anschlüsse             |
| **Hauptfunktion**         | Automatisierter Excel-Import + Adress-Management  | 16-Spalten-Format aus Anschlussprojekt-Daten  |
| **Zeitersparnis**         | 75% weniger Dokumentationszeit                    | 4 Stunden/Woche pro Verkäufer                 |
| **ROI**                   | 5.000€/Jahr bei 25€/Stunde                        | Produktivitätssteigerung messbar              |
| **Compliance**            | 100% DSGVO-konform, lokal verarbeitet             | Verschlüsselte Speicherung, Audit-Logs        |

### Technologie & Sicherheit
- **Lokale Installation:** Spring Boot + PostgreSQL + Keycloak, keine Cloud-Abhängigkeit
- **Automatisierung:** Wöchentlicher Excel-Import (.xlsx) mit 16-Spalten-Format
- **Sichere Authentifizierung:** Keycloak Realm `simple-salesman-backend`
- **DSGVO-Compliance:** Verschlüsselte Datenhaltung, Recht auf Vergessenwerden

### Entwicklungsstand (v0.0.5 - 16.06.2025) - **GitHub-Reality**
✅ **Backend produktionsreif:** Vollständige REST-API mit Postman-Collection  
✅ **Excel-Import funktional:** .xlsx Anschlussprojekt-Daten automatisiert  
✅ **CRUD komplett:** Projekte, Adressen, Notizen mit Unit-Tests  
✅ **Wetter-Integration:** wttr.in API über /api/v1/weather implementiert  
🔄 **Keycloak:** Technisch eingebunden, aber noch **NICHT aktiviert**  
❌ **Frontend:** **Kein Web-UI vorhanden** - komplette Entwicklung steht aus  
❌ **DSGVO-Tools:** Logging, Protokollierung, Rechteverwaltung **fehlen noch**  
❌ **Admin-Konsole:** Benutzerübersicht **nicht implementiert**

---

## 📋 Lastenheft - Anforderungen

### Stakeholder & Prioritäten
**Verkäufer (Hauptnutzer):** Adress-basierte D2D-Arbeit, Notizen-Erfassung  
**Administrator:** System- und Benutzerverwaltung, Import-Überwachung  
**DSGVO-Verantwortlicher:** Compliance-Überwachung, Audit-Trails  
**Geschäftsführung:** ROI-Tracking, Effizienzsteigerung

### MUSS-Anforderungen (Kritisch)

**L1: Excel-Anschlussprojekt-Import**  
Als Verkäufer möchte ich wöchentlich Excel-Dateien mit Anschlussprojekt-Daten hochladen können, damit verkaufsfähige Adressen automatisch identifiziert werden.
- Upload von .xlsx-Dateien mit 16-Spalten-Format (A: adrcd-subcd bis P: Outdoor-Pauschale)
- Automatische Adress-Extraktion aus Spalte E ("Straße Nummer, PLZ, Ort")
- Filterung verkaufsfähiger Adressen (construction_completed = true)
- Datenerhalt: Bestehende Datensätze bleiben erhalten

**L2: Benutzer-Authentifizierung**  
Als Verkäufer möchte ich mich sicher über Keycloak anmelden können, damit nur autorisierte Personen Zugang haben.
- Lokale Keycloak-Realm `simple-salesman-backend`
- Rollenbasierte Zugriffskontrolle (USER, ADMIN)
- Session-Management mit automatischer Abmeldung

**L3: Adress-zentrierte Verwaltung**  
Als Verkäufer möchte ich Adressen mit Status-Tracking verwalten können, damit ich meinen D2D-Fortschritt nachverfolgen kann.
- CRUD-Operationen für Adressen mit verschlüsselter Speicherung
- Status-Updates (Offen → Besucht → Vertrag/Absage)
- Verknüpfung mit Anschlussprojekt-Hintergrundinformationen

**L4: Notizen-System pro Adresse**  
Als Verkäufer möchte ich zu jeder Adresse strukturierte Notizen erfassen können, damit Kundeninteraktionen dokumentiert sind.
- Kategorisierte Notizen (Besuch, Telefonat, Nachfassung)
- Zeitstempel und Benutzer-Zuordnung
- Verschlüsselte Speicherung (DSGVO-konform)

### KANN-Anforderungen (Wichtig)

**L5: Dashboard & moderne UI** (**NICHT IMPLEMENTIERT**)  
Als Verkäufer möchte ich eine übersichtliche, responsive Benutzeroberfläche haben.
❌ **Status:** Kein Web-Frontend vorhanden
❌ Responsive Design, Dark Mode - **zu entwickeln**
❌ Kartenbasierte und Tabellen-Darstellung - **zu entwickeln**

**L6: Wetter-Integration** ✅ **BACKEND IMPLEMENTIERT**  
Als Verkäufer möchte ich das aktuelle Wetter sehen.
✅ Backend: /api/v1/weather über wttr.in API funktional
❌ Frontend: Kein UI-Widget vorhanden

**L7: Such- und Filterfunktionen** ✅ **BACKEND IMPLEMENTIERT**  
Als Verkäufer möchte ich Adressen durchsuchen und filtern können.
✅ Backend: REST-API mit Filter-Parametern
❌ Frontend: Keine Benutzeroberfläche

### DSGVO-Compliance-Anforderungen (**NICHT IMPLEMENTIERT**)
❌ **DS1:** Protokollierung - **fehlt noch**  
❌ **DS2:** Verschlüsselte Speicherung - **fehlt noch**  
❌ **DS3:** Zugriffskontrolle - **fehlt noch**  
❌ **DS4:** Recht auf Vergessenwerden - **fehlt noch**  
❌ **DS5:** Datenschutzerklärung - **fehlt noch**

### Apache License Compliance (MUSS)
**LI1:** Lizenzhinweise im Quellcode, **LI2:** Copyright-Hinweise Dritter, **LI3:** Open-Source-Komponenten dokumentiert

---

## 🔧 Pflichtenheft - Technische Umsetzung

### System-Architektur
```
┌─────────────────────────────────────────────────────────────┐
│ Frontend: Thymeleaf Templates + Static Resources            │
│ • layout.html (Basis-Template)                              │
│ • Responsive CSS, Dark Mode Support                         │
│ • JavaScript für dynamische Inhalte                         │
└─────────────────┬───────────────────────────────────────────┘
                  │ HTTP/HTTPS
┌─────────────────▼───────────────────────────────────────────┐
│ Backend: Spring Boot Application                            │
│ • REST-Controller für API-Endpoints                         │
│ • Thymeleaf-Controller für Template-Rendering               │
│ • Service-Layer für Geschäftslogik                          │
└─────────────────┬───────────────────────────────────────────┘
                  │ JPA/Hibernate
┌─────────────────▼───────────────────────────────────────────┐
│ PostgreSQL Database (Schema auto-generiert)                 │
│ • JPA @Entity Annotations                                   │
│ • Automatische Tabellenerstellung                           │
└─────────────────────────────────────────────────────────────┘
```

### Datenmodell (JPA-Entities - **GitHub v0.0.5**)

**Implementierte Entities (vereinfacht):**
```java
@Entity
public class Address {
    @Id Long id;
    String street, city, postalCode;
    // Weitere Felder basierend auf Excel-Import
    @OneToMany List<Note> notes;
    @OneToMany List<Project> projects;
}

@Entity 
public class Project {
    @Id Long id;
    String title, description;
    @Enumerated ProjectStatus status;
    @ManyToOne Address address;
}

@Entity
public class Note {
    @Id Long id;
    String content;
    LocalDateTime created;
    @ManyToOne Address address;
    @ManyToOne Project project;
}
```

**❌ NICHT IMPLEMENTIERT:**
- DSGVO-Verschlüsselung (keine `byte[]` encrypted fields)
- Audit-Logging Entities
- Benutzer-Zuordnung (`assignedTo` etc.)

### Sicherheitskonzept (**TEILWEISE IMPLEMENTIERT**)

**Keycloak-Vorbereitung:**
```java
// Keycloak ist technisch eingebunden, aber NICHT aktiviert
// Konfiguration vorhanden, aber noch nicht produktiv
keycloak.realm=simple-salesman-backend  // vorbereitet
keycloak.auth-server-url=http://localhost:8081  // konfiguriert
```

**❌ NICHT IMPLEMENTIERT:**
- Aktive OAuth2-Authentifizierung
- Rollen-basierte Autorisierung
- DSGVO-Verschlüsselung
- Audit-Logging
- Session-Management

### Frontend-Struktur (**NICHT VORHANDEN**)

**❌ Aktueller Stand:**
```
/templates/     - LEER (kein Web-UI)
/static/        - LEER (keine CSS/JS)
```

**❌ NICHT IMPLEMENTIERT:**
- Thymeleaf-Templates
- HTML/CSS/JavaScript
- Responsive Design
- Dark Mode
- Dashboard-UI
- Wetter-Widget UI

### Frontend-Struktur
**Template-Architektur:**
```
/templates/
├── layout.html                 # Basis-Template (Header, Nav, Footer)
├── dashboard/
│   ├── index.html             # Hauptdashboard mit Karten-Layout
│   └── weather-widget.html     # Wetter-Integration
├── projects/
│   ├── list.html              # Projekt-Übersicht (Tabelle)
│   ├── detail.html            # Projekt-Detailansicht
│   └── form.html              # Projekt-Erfassung/Bearbeitung
├── addresses/
│   ├── list.html              # Adress-Übersicht
│   └── detail.html            # Adress-Detailansicht mit Notizen
└── notes/
    └── form.html              # Notiz-Editor
```

**Statische Ressourcen:**
```
/static/
├── css/
│   ├── main.css               # Basis-Styles, Responsive Design
│   ├── dark-mode.css          # Dark Theme Varianten
│   └── components.css         # UI-Komponenten (Karten, Tabellen)
├── js/
│   ├── app.js                 # Haupt-JavaScript
│   ├── weather.js             # Geolocation & Wetter-API
│   └── theme.js               # Dark Mode Toggle
└── assets/
    ├── icons/                 # SVG Icons
    └── avatars/               # Benutzer-Avatare
```

### REST-API Endpoints (**GitHub v0.0.5 - Implementiert**)
| Endpoint                          | Methode                | Zweck                        | Testing     |
|-----------------------------------|----------------------------------------------------------------------
| `/api/v1/addresses`               | GET, POST, PUT, DELETE | Adress-CRUD                  |  ✅ Postman |
| `/api/v1/projects`                | GET, POST, PUT, DELETE | Projekt-CRUD                 |  ✅ Postman |
| `/api/v1/notes`                   | GET, POST, PUT, DELETE | Notizen-CRUD                 |  ✅ Postman |
| `/api/v1/import/excel`            | POST                   |.xlsx Upload und Verarbeitung |  ✅ Postman |
| `/api/v1/weather?region={region}` | GET                    | Wetter-Abfrage (wttr.in)     |  ✅ Postman |

**❌ NICHT IMPLEMENTIERT:**
- `/api/v1/addresses/my-workload` (Benutzer-spezifische Filter)
- `/api/v1/audit` (DSGVO-Audit-Logs)
- Authentifizierung-geschützte Endpoints

### Excel-Import-Workflow (**Implementiert**)
```
1. .xlsx Upload → Apache POI Parser                                     ✅
2. Daten-Extraktion → Entity-Mapping                                    ✅  
3. Validation → PostgreSQL Speicherung                                  ✅
4. Import-Log → Response                                                ✅
```

**Funktional:** Vollständig getestet mit Postman-Collection

### Wetter-Integration (**Backend implementiert**)
```java
// REST-Controller implementiert
@GetMapping("/api/v1/weather")
public WeatherResponse getWeather(@RequestParam String region) {
    // wttr.in API Integration - FUNKTIONAL
}
```

**❌ Frontend-Integration:** Kein JavaScript/UI vorhanden

### Deployment & Installation (**Backend-Ready**)
**Installation:**
```bash
1. PostgreSQL: createdb simplesalesman                                  ✅
2. Application: java -jar simple-salesman.jar                           ✅ 
   (Auto-Schema-Generierung funktional)
3. Keycloak: Realm-Import noch nicht aktiviert                          ❌
```

**Produktive Konfiguration:**
```properties
# Funktional (Backend)
spring.jpa.hibernate.ddl-auto=update                                    ✅
spring.datasource.url=jdbc:postgresql://localhost:5432/simplesalesman   ✅

# Nicht aktiviert
keycloak.realm=simple-salesman-backend                                  ❌
app.encryption.key=${ENCRYPTION_KEY}                                    ❌
```

### Aktuelle Implementierung (**GitHub v0.0.5 Reality Check**)
✅ **Backend komplett:**    Services, Repositories, Controller mit Unit-Tests  
✅ **Excel-Import:**        .xlsx Verarbeitung mit Apache POI funktional  
✅ **REST-API:**            Alle CRUD-Operationen via Postman getestet  
✅ **Wetter-API:**          wttr.in Integration funktional  
✅ **Database:**            PostgreSQL mit JPA Auto-Schema-Generierung  
❌ **Frontend:**            Komplett fehlend - keine HTML/CSS/JS Dateien  
❌ **Keycloak:**            Technisch vorbereitet, aber nicht aktiviert  
❌ **DSGVO:**               Verschlüsselung und Audit-Logging nicht implementiert  
❌ **Admin-Tools:**         Benutzerübersicht und Verwaltung fehlen  

**Test-Status:** Backend vollständig über Postman testbar, **kein Browser-UI verfügbar**

---

**Projektstatus:**               Backend produktionsreif (60% Gesamt-Komplett) 
• **Entwicklungszeit:**          36h geleistet 
• **Testing:**                   Backend vollständig via Postman getestet 
• **Kritische Lücken:**          ❌ Kein Frontend-UI ❌ Keycloak nicht aktiviert ❌ DSGVO-Tools fehlen 
• **Nächste Schritte:**          Frontend-Entwicklung (HTML/CSS/JS) + Keycloak-Aktivierung + DSGVO-Implementierung (verbleibende 40% in 3-4 Wochen) 
• **ROI:**                       5.000€/Jahr erreichbar nach Komplettierung
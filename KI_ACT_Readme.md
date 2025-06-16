# Simple Salesman – KI-Dokumentation gemäß EU AI Act

## Projektübersicht

**Anwendungsname:** Simple Salesman  
**Version:** 0.1  
**Zweck:** Verwaltung von Adressen, Notizen und Auftragsstatus für Door-to-Door-Vertrieb  
**Verantwortlich:** Yaroslav Vol (Privatperson, Open Source)  
**Technologiestack:** Spring Boot, PostgreSQL, Apache POI, Keycloak, HTML/JavaScript  

---

## Dokumentation der KI-Funktionen gemäß EU AI Act

### Tatsächlich implementierte Komponenten

**Wetterdienst**  
- Beschreibung: REST-basierte Abfrage über wttr.in  
- Lokale Verarbeitung: Nein (extern)  
- Modellversion: –  
- Tokenverwendung: Nein  

> ⚠️ Hinweis: Der KI-Texteditor mit GPT-2 wurde in der Planung berücksichtigt, aber noch nicht implementiert. Es existieren keine GPT-Modelle im Code.

---

## Zweck der geplanten KI-Nutzung (zukünftig)

- Textvorschläge beim Schreiben von Notizen  
- Keine Bewertung oder Automatisierung von Entscheidungen  
- Lokal gehostetes Modell (geplant: GPT-2)  
- KI-Option abschaltbar (Opt-out im Frontend vorgesehen)  

---

## Tokenbasierte Verarbeitung (implementiert)

**Keycloak JWT**  
- Inhalt: Rollen, Benutzer-ID, Berechtigungen  
- Speicherdauer: Sitzungslaufzeit  
- Zweck: Authentifizierung und Zugriffskontrolle  

---

## Datenschutz & Sicherheit (DSGVO)

**DS1 – Logging**  
Aktiv durch Spring Boot Logging  

**DS2 – Verschlüsselung**  
Geplant, derzeit noch nicht aktiv im Code  

**DS3 – Zugriffsschutz**  
Umgesetzt über Keycloak  

**DS4 – Löschfunktion**  
Nur teilweise vorhanden, API-Grundstruktur implementiert  

**DS5 – Einwilligung**  
Frontend-Integration geplant, Datenschutzerklärung derzeit manuell  

---

## Konformität mit dem EU AI Act (IST-Zustand)

**KI1**  
Keine aktiven KI-Funktionen vorhanden → nicht erforderlich  

**KI2**  
Opt-out im Frontend vorgesehen → geplant  

**KI3**  
Keine KI-Datenverarbeitung → erfüllt  

**KI4**  
Modellbeschreibung vorbereitet, aber noch nicht umgesetzt → geplant  

---

## Genutzte Bibliotheken & Lizenzen

- Apache POI (Apache License 2.0): Excel-Import  
- Spring Boot (Apache License 2.0): REST-Backend  
- Keycloak (Apache License 2.0): Authentifizierung  
- MapStruct (Apache License 2.0): DTO-Mapping  

---

## Änderungsprotokoll (Stand Version 0.1)

- Wetter-API via wttr.in integriert (WeatherClient)  
- Excel-Importservice über Apache POI umgesetzt  
- Postman-Tests für API vorbereitet  
- Keycloak-Integration für Authentifizierung (JWT)  
- Notizen-API mit Dummy-Daten implementiert  

---

## Entwicklungsstand

### Bereits umgesetzt

- REST-API (CRUD für Adressen und Notizen)  
- Excel-Import via Controller  
- Wetterdienst-Integration  
- Keycloak-Login mit Token-Handling im Frontend  
- Dummy-Frontend mit:
  - Dark Mode  
  - Datentabelle  
  - Avatar-Upload  
  - Wetteranzeige via Geolocation  
- Responsives Frontend mit Icons und eigener Filtersuche  

### In Planung (aus Chatverlauf abgeleitet)

- Lokaler GPT-2-Texteditor  
- Datenverschlüsselung (PostgreSQL)  
- Adminbereich mit Backup- und Protokollfunktionen  
- Gamification-Elemente (z. B. Easter Eggs, Fortschrittsanzeige)  
- CI/CD-Pipeline und GitHub-Deployment  
- MVP-Abschluss bis 27.06.2025  

---

## Bewertung

- **DSGVO-Grundschutz aktiv:** Logging, Rollen, Zugriffssicherheit  
- **KI-Dokumentation vorhanden:** Einsatz nur geplant, nicht aktiv  
- **MVP-Status:** Vollständig lokal betrieben, datenschutzfreundlich und erweiterbar  


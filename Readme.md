
# Simple Salesman

![version](https://img.shields.io/badge/version-0.0.3-blue)

Backend-Prototyp für eine lokale Webanwendung zur Verwaltung und Dokumentation von Adressdaten, Notizen und Auftragsstatus für Door-to-Door-Vertriebsteams.

---

## Version

Aktuell: 0.0.3  
Letztes Update: 03.06.2025


Update 0.0.3  
-Bis Spring Boot 2.3 wurde in manchen Starter-Setups (insb. mit spring-boot-starter-web) automatisch ein RestTemplate-Bean mitgeliefert.
Ab Spring Boot 2.4+ NICHT MEHR!
Jetzt musst du explizit selbst einen RestTemplate-Bean definieren, sonst gibt’s diesen Fehler.

---

## Aktueller Entwicklungsstand

- **Backend-Prototyp** ist lauffähig und getestet
- **Excel-Import** von Adress- und Projektdaten ist implementiert
- **Entitäten, DTOs, Mapper, Repositories und Services** sind erstellt und getestet
- **Unit-Tests** für Mapper, Services und Import vorhanden
- **Keycloak-Integration und Security** vorbereitet, aber noch nicht produktiv nutzbar
- **Kein produktives Frontend:** Es existiert aktuell **keine** Weboberfläche zur Bedienung!
- **Weitere Features** (DSGVO, Wetter, KI, Adminfunktionen) sind geplant, aber noch nicht umgesetzt

---

## Features (Backend, Stand 0.0.2)

- Import von Excel-Dateien ins Datenbankschema (PostgreSQL)
- Speicherung, Abfrage und Bearbeitung von Adressdaten, Projekten und Notizen
- Mapping von Entitäten zu DTOs (inkl. Tests)
- Grundstruktur für Benutzer- und Rechteverwaltung angelegt
- Unit-Tests für zentrale Geschäftslogik und Import
- Projektstruktur nach Best Practices (Clean Architecture, Maven)

---

## Nicht enthalten / in Planung

- Es existiert **kein Web-Frontend** (kein Datei-Upload, keine UI für Notizen/Adressen)
- Keycloak-Anbindung und Adminfunktionen sind nicht produktiv
- Keine DSGVO-Funktionen (Löschprotokoll, Logging, etc.)
- Keine KI-Integration oder Wetterdaten im Live-Betrieb

---

## Getting Started (nur Backend)

Voraussetzungen:
- Java 17
- Maven
- PostgreSQL
- Optional: Keycloak für spätere Authentifizierung

Klonen:
```bash
git clone https://github.com/Yaro-bit/SimpleSalesman.git
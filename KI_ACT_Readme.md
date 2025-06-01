# Simple Salesman – KI-Dokumentation gemäß EU AI Act

## Projektübersicht

**Anwendungsname:** Simple Salesman  
**Version:** 0.1  
**Zweck:** Verwaltung von Adressen und Notizen im Door-to-Door-Vertrieb  
**Verantwortlich:** Yaroslav Vol (Privatperson, Open Source)  
**Technologiestack:** Spring Boot, PostgreSQL, Apache POI, Keycloak, HTML/JavaScript


## Dokumentation der KI-Funktionen gemäß EU AI Act

### Eingesetzte Komponenten

| Komponente          |Beschreibung                                                               | Lokale Verarbeitung | Modellversion  | Tokenverwendung |
|---------------------|---------------------------------------------------------------------------|---------------------|----------------|-----------------|
| KI-Texteditor       | Optionales Modul zur Generierung von Notizvorschlägen                     | Ja                  | GPT-2 (lokal)  | Ja (RAM)        |
| Wetterdienst        | REST-basierte Anzeige aktueller Wetterdaten (kein KI-System)              | Nein (extern)       | –              | Nein            |

### Zweck der KI-Nutzung

- Unterstützung beim effizienten Verfassen von Notizen durch strukturierte Textvorschläge
- Keine automatisierte Entscheidungsfindung oder Bewertung
- Verarbeitung ausschließlich lokal – keine Kommunikation mit externen KI-Diensten

### Tokenbasierte Verarbeitung

| Quelle        | Inhalt                                    | Speicherdauer        | Zweck                              |
|---------------|-------------------------------------------|----------------------|------------------------------------|
| KI-Editor     | Tokenisierter Eingabetext für Vorschläge  | Temporär im RAM      | Generierung kontextbezogener Texte |
| Keycloak JWT  | Rollen, Benutzer-ID, Berechtigungen       | Sitzungslaufzeit     | Authentifizierung, Logging         |

---

## Datenschutz & Sicherheit (DSGVO)

| Anforderung         | Umsetzung                                                                  |
|---------------------|----------------------------------------------------------------------------|
| DS1: Logging        | Vollständige Protokollierung von Datenzugriffen und Änderungen             |
| DS2: Verschlüsselung| AES-256-Verschlüsselung sämtlicher sensibler Nutzerdaten                   |
| DS3: Zugriffsschutz | Zugriff auf personenbezogene Daten ausschließlich durch berechtigte Nutzer |
| DS4: Löschfunktion  | Umsetzung des Rechts auf Vergessenwerden durch vollständige Datenlöschung  |
| DS5: Einwilligung   | Anzeige einer Datenschutzerklärung mit aktiver Nutzerzustimmung            |

---

## Konformität mit dem EU AI Act

| Anforderung | Beschreibung                                                         | Status   |
|-------------|----------------------------------------------------------------------|----------|
| KI1         | KI-generierte Inhalte sind klar als solche gekennzeichnet            | Erfüllt  |
| KI2         | Die KI-Funktion kann deaktiviert werden (Opt-out-Option im UI)       | Erfüllt  |
| KI3         | Keine Speicherung oder Weiterverarbeitung personenbezogener Daten    | Erfüllt  |
| KI4         | Modellversion und Zweck sind dokumentiert (lokale GPT-2-Variante)    | Erfüllt  |

---

## Verwendete Bibliotheken & Lizenzen

| Komponente        | Lizenz             | Einsatzzweck                                  |
|-------------------|--------------------|-----------------------------------------------|
| Apache POI        | Apache License 2.0 | Import und Export von Excel-Dateien           |
| Spring Boot       | Apache License 2.0 | Web-Backend und REST-API                      |
| Keycloak          | Apache License 2.0 | Authentifizierung und rollenbasierte Zugriffe |
| MapStruct         | Apache License 2.0 | Automatisches Mapping zwischen DTOs und Ent.  |

---

## Änderungen & Nachvollziehbarkeit

Alle KI-bezogenen Änderungen werden im CHANGELOG.md dokumentiert, inklusive:

- Änderungsdatum
- Beschreibung und Zweck
- Verwendete Modellversion (z. B. GPT-2 lokal)
- Referenz zur Commit-ID (sofern öffentlich verfügbar)

---

## Historie der Anforderungen & Entwicklungsziele

Diese Sektion dokumentiert die fortlaufende Weiterentwicklung des Projekts anhand von User Stories, Feedback und technischen Anforderungen.

### Projektkontext

- **Autor:** Yaroslav Vol (Privatperson, kein Unternehmen)
- **Zielgruppe:** Zwei Außendienstmitarbeiter und ein Entwickler
- **Rechtsrahmen:** DSGVO, EU AI Act
- **Veröffentlichung:** Open Source (Apache License 2.0)

### Hauptanforderungen

- Automatisierter Excel-Import (Apache POI)
- PostgreSQL-Datenbank mit verschlüsselter Speicherung
- Notizsystem mit Benutzer- und Zeitstempeln
- Frameworkfreies Web-Frontend mit Dark Mode
- REST-basierter Wetterdienst
- KI-basierte Textvorschläge (lokal, optional)
- Benutzerverwaltung mit Keycloak (lokal gehostet)
- Klare API-Struktur nach Clean Architecture
- Testabdeckung mittels JUnit (Unit & Integration)
- Gamification-Elemente (Easter Eggs, Motivationstracking)
- Vollständige DSGVO-Konformität
- Dokumentation relevanter Stakeholder- und Feature-Anforderungen
- KI-Dokumentation gemäß EU AI Act
- SMART-Zielsetzung und technisches To-Do-Board
- Ziel: Bereitstellung eines Minimum Viable Product bis 27.06.

### Softwarecharakter

- Kein Cloud-Zwang, kein SaaS-Modell
- Lokale Nutzung mit Fokus auf Datenschutz und Kontrolle
- Vollständig quelloffen, für kleine Teams optimiert

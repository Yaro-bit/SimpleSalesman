# 📄 AddressController.java

**Paket:** `com.simplesalesman.controller`

## Beschreibung
Stellt REST-Endpunkte zur Verwaltung von Adressen bereit. Unterstützt CRUD-Operationen, sowie das Abrufen verknüpfter Projekte oder Notizen.

## Hauptmethoden
- `GET /api/v1/addresses` – Liste aller Adressen
- `GET /api/v1/addresses/{id}` – Einzeladresse
- `POST /api/v1/addresses` – Neue Adresse anlegen
- `PUT /api/v1/addresses/{id}` – Adresse aktualisieren
- `DELETE /api/v1/addresses/{id}` – Adresse löschen

## Kommentar für DEV
> Schreibe einen Integrationstest, der eine Adresse erstellt, anschließend abruft, aktualisiert und löscht. Überprüfe dabei HTTP-Statuscodes, JSON-Feldinhalte und Fehlerfälle (z. B. ID nicht vorhanden).
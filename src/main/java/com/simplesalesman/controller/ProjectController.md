# 📄 ProjectController.java

**Paket:** `com.simplesalesman.controller`

## Beschreibung
Behandelt REST-Endpunkte rund um Projekte, insbesondere Statusaktualisierungen, Verknüpfung mit Adressen, Baufirma & Betreiber.

## Hauptmethoden
- `GET /api/v1/projects`
- `POST /api/v1/projects`
- `PUT /api/v1/projects/{id}/status` – Status ändern
- `GET /api/v1/projects/address/{addressId}` – Alle Projekte zu einer Adresse

## Kommentar für DEV
> Simuliere die Erstellung eines Projekts mit Bezug zu einer Adresse. Teste die Statusänderung per PUT-Request. Überprüfe, ob nur gültige Status akzeptiert werden.
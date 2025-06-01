# 📄 AuthController.java

**Paket:** `com.simplesalesman.controller`

## Beschreibung
Stellt Nutzerinformationen bereit, die aus dem JWT-Token über Keycloak extrahiert werden. Wird vom Frontend für personalisierte Inhalte genutzt.

## Hauptmethoden
- `GET /api/v1/user/me` – Aktueller Benutzer
- `GET /api/v1/user/roles` – Rollenauflistung

## Kommentar für DEV
> Simuliere authentifizierte und nicht authentifizierte Anfragen. Überprüfe die Rückgabe der Benutzerinformationen auf Richtigkeit und Zugriffsrechte.
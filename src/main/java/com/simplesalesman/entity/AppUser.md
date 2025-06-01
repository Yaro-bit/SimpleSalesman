# 📄 AppUser.java

**Paket:** `com.simplesalesman.entity`

## Beschreibung
Repräsentiert einen Benutzer in der Anwendung, oft synchronisiert mit einem Keycloak-Account.

## Felder (typisch)
- ID (Keycloak-Referenz)
- Name, Rollen
- Beziehungen zu Notizen oder Adressen (optional)

## Kommentar für DEV
> Teste die Speicherung eines AppUser-Objekts mit Keycloak-ID. Stelle sicher, dass Zuordnungen zu Notizen oder Adressen möglich sind (falls vorhanden).
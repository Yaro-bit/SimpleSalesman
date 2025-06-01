# 📄 KeycloakConfig.java

**Paket:** `com.simplesalesman.config`

## Beschreibung
Dient als Platzhalter für optionale Keycloak-Konfigurationen.
Die zentrale Security-Konfiguration erfolgt über die application.properties (spring.security.oauth2.resourceserver.jwt.issuer-uri).

## Kommentar für DEV
> Keine Konfigurationslogik enthalten.
Für Standard-Setup: Keine Tests notwendig.
Für Anpassungen (z. B. Custom JWT Converter) muss hier entsprechende Logik ergänzt und getestet werden.
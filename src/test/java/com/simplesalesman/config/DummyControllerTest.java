package com.simplesalesman.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DummyController für Security-Tests.
 *
 * Stellt minimalistische Endpunkte bereit, um die Zugriffskontrolle (SecurityConfig)
 * mit MockMvc testen zu können. Enthält keine Geschäftslogik.
 *
 * - "/"                 → Öffentlicher Endpunkt für Basistests.
 * - "/api/v1/import"    → Geschützter Endpunkt, für Rollen- und Authentifizierungstests.
 *
 * Hinweis: Nur für Testzwecke im Testkontext verwenden.
 */
@RestController
class DummyControllerTest {

    /**
     * Öffentlicher Endpunkt (keine Authentifizierung erforderlich).
     */
    @GetMapping("/")
    public String publicEndpoint() {
        return "ok";
    }

    /**
     * Geschützter Endpunkt für Security-Tests (z. B. Rollenprüfung).
     */
    @GetMapping("/api/v1/import")
    public String importEndpoint() {
        return "import";
    }
}

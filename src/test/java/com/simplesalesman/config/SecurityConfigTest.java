package com.simplesalesman.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testklasse für SecurityConfig.
 *
 * Testet die Zugriffskontrolle (Authentifizierung/Rollen) auf Beispielendpunkte
 * des DummyControllers mit MockMvc. Es werden ausschließlich Security-Aspekte
 * geprüft, keine Fachlogik.
 *
 * Hinweise:
 * - @WebMvcTest lädt nur die Web-/Security-Schicht mit DummyController.
 * - @Import(SecurityConfig.class) bindet die Security-Konfiguration ein.
 *
 * Quelle: Spring Security Reference Documentation, Kapitel „Testing with MockMvc“
 * https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/index.html
 */
@WebMvcTest(controllers = DummyControllerTest.class) // Lädt nur DummyController (Test-Endpunkte)
@Import(SecurityConfig.class)                    // Security-Konfiguration aktivieren
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedAccessToApiShouldBeDenied() throws Exception {
        // Prüft, ob nicht authentifizierte Zugriffe auf geschützte Endpunkte abgelehnt werden (401 Unauthorized)
        mockMvc.perform(get("/api/v1/import"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminAccessToImportShouldBeAllowed() throws Exception {
        // Prüft, ob ein Benutzer mit Rolle ADMIN Zugriff auf geschützten Endpunkt erhält (200 OK)
        mockMvc.perform(get("/api/v1/import"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userAccessToImportShouldBeForbidden() throws Exception {
        // Prüft, ob ein Benutzer ohne Rolle ADMIN abgelehnt wird (403 Forbidden)
        mockMvc.perform(get("/api/v1/import"))
                .andExpect(status().isForbidden());
    }

    @Test
    void publicEndpointShouldBeAccessible() throws Exception {
        // Prüft, ob ein öffentlicher Endpunkt ohne Authentifizierung erreichbar ist (200 OK)
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}

package com.simplesalesman.controller;

import com.simplesalesman.dto.UserDto;
import com.simplesalesman.entity.AppUser;
import com.simplesalesman.mapper.UserMapper;
import com.simplesalesman.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    // MockMvc zum Simulieren von HTTP-Anfragen
    @Autowired
    private MockMvc mockMvc;

    // Mock des UserRepository, um DB-Zugriffe zu simulieren
    @SuppressWarnings("removal")
	@MockBean
    private UserRepository userRepository;

    // Mock des UserMapper, um Entity nach DTO umzuwandeln
    @SuppressWarnings("removal")
	@MockBean
    private UserMapper userMapper;

    /**
     * Testet erfolgreichen Abruf eines existierenden Nutzers über den Endpunkt "/api/v1/auth/me".
     */
    @Test
    void getCurrentUser_returnsUserDto_whenUserExists() throws Exception {
        // Vorbereitung: Keycloak-ID des Nutzers festlegen
        String keycloakId = "12345";

        // AppUser-Entity zur Simulation der DB-Abfrage erstellen
        AppUser appUser = new AppUser();
        appUser.setKeycloakId(keycloakId);
        appUser.setUsername("testuser");

        // Erwartetes UserDto, das vom Mapper zurückgegeben wird
        UserDto userDto = new UserDto();
        userDto.setKeycloakId(keycloakId);
        userDto.setUsername("testuser");
        userDto.setRole("USER");
        userDto.setActive(true);

        // Verhalten der gemockten Komponenten definieren
        Mockito.when(userRepository.findByKeycloakId(anyString()))
               .thenReturn(Optional.of(appUser));
        Mockito.when(userMapper.toDto(appUser))
               .thenReturn(userDto);

        // HTTP-Anfrage simulieren und Ergebnis prüfen
        mockMvc.perform(get("/api/v1/auth/me")
                .with(jwt().jwt(jwt -> jwt.subject(keycloakId)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keycloakId").value(keycloakId))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.active").value(true));
    }

    /**
     * Testet das Verhalten, wenn kein Nutzer mit der angegebenen Keycloak-ID existiert.
     * Erwartet wird aktuell ein HTTP-500-Fehler, da RuntimeException geworfen wird.
     * (Empfohlen: Stattdessen einen spezifischen Exception-Handler implementieren.)
     */
    @Test
    void getCurrentUser_returnsError_whenUserDoesNotExist() throws Exception {
        // Vorbereitung: Keycloak-ID eines nicht existierenden Nutzers
        String keycloakId = "unknown";

        // Verhalten des Repositories simulieren: kein Nutzer gefunden
        Mockito.when(userRepository.findByKeycloakId(anyString()))
               .thenReturn(Optional.empty());

        // HTTP-Anfrage simulieren und Statuscode prüfen
        mockMvc.perform(get("/api/v1/auth/me")
                .with(jwt().jwt(jwt -> jwt.subject(keycloakId)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}

/*
 * DEV:
Die Tests validieren, ob der Controller korrekt mit gültigen und ungültigen JWTs umgeht und entsprechend User-Daten liefert.
Prüfe regelmäßig, ob der Fehlerstatus-Code (500) bei fehlendem Nutzer sinnvoll ist. Besser wäre möglicherweise ein spezifischer Exception-Handler,
der einen Status 404 Not Found zurückgibt.
 */


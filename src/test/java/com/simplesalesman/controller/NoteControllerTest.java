package com.simplesalesman.controller;

import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.service.NoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser; // WICHTIG: Import für Security-Simulation

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private NoteService noteService;

    /**
     * Testet den GET-Endpunkt zur Abfrage aller Notizen für eine Adresse.
     * Mit @WithMockUser wird ein authentifizierter Benutzer simuliert.
     * Erwartet eine Liste von Notizen als JSON-Antwort.
     */
    @Test
    @WithMockUser(username = "testuser") // <- Simuliert einen eingeloggten User (wichtig für Security!)
    @DisplayName("GET /api/v1/notes/{addressId} returns notes for address")
    void getNotesForAddress_returnsNotesList() throws Exception {
        // Testdaten anlegen
        NoteDto note1 = new NoteDto();
        note1.setId(1L);
        note1.setText("Test note 1");
        note1.setCreatedAt(LocalDateTime.of(2024, 5, 30, 12, 0));
        note1.setCreatedBy("Max");

        NoteDto note2 = new NoteDto();
        note2.setId(2L);
        note2.setText("Test note 2");
        note2.setCreatedAt(LocalDateTime.of(2024, 5, 31, 9, 30));
        note2.setCreatedBy("Anna");

        // Service wird gemockt, gibt die Testdaten zurück
        Mockito.when(noteService.getNotesForAddress(42L))
                .thenReturn(Arrays.asList(note1, note2));

        // GET-Request ausführen und prüfen, ob die Antwort wie erwartet aussieht
        mockMvc.perform(get("/api/v1/notes/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].text").value("Test note 1"))
                .andExpect(jsonPath("$[0].createdBy").value("Max"))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].text").value("Test note 2"))
                .andExpect(jsonPath("$[1].createdBy").value("Anna"))
                .andExpect(jsonPath("$[1].createdAt").exists());
    }

    /**
     * Testet den GET-Endpunkt, wenn für eine Adresse keine Notizen existieren.
     * @WithMockUser simuliert einen eingeloggten User.
     * Erwartet wird ein leeres Array als JSON.
     */
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("GET /api/v1/notes/{addressId} returns empty list if no notes found")
    void getNotesForAddress_returnsEmptyList() throws Exception {
        Mockito.when(noteService.getNotesForAddress(anyLong()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/notes/999"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    /**
     * Testet den POST-Endpunkt zum Hinzufügen einer Notiz zu einer Adresse.
     * @WithMockUser simuliert einen eingeloggten User.
     * Es wird geprüft, ob ein Ok-Status (200) und ein leerer Body zurückkommen.
     */
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("POST /api/v1/notes/{addressId} adds a new note to address")
    void addNoteToAddress_returnsOk() throws Exception {
        Mockito.doNothing().when(noteService)
                .addNoteToAddress(eq(42L), eq("Neuer Eintrag"), eq("Max"));

        String json = "{\"text\": \"Neuer Eintrag\", \"createdBy\": \"Max\"}";

        mockMvc.perform(post("/api/v1/notes/42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())) // <- CSRF-Token für POST
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}

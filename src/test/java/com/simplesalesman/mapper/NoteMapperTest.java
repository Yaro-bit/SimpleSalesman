package com.simplesalesman.mapper;

import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.entity.Note;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NoteMapperTest {

    private final NoteMapper noteMapper = new NoteMapper();

    @Test
    @DisplayName("toDto() überträgt alle Felder korrekt von Note nach NoteDto")
    void toDto_shouldMapAllFields() {
        // Arrange: Beispiel-Note anlegen
        Note note = new Note();
        note.setId(42L);
        note.setText("Test-Notiz");
        note.setCreatedAt(LocalDateTime.of(2024, 5, 31, 8, 0));
        note.setCreatedBy("Max Mustermann");

        // Act: Mapping durchführen
        NoteDto dto = noteMapper.toDto(note);

        // Assert: Felder prüfen
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(42L);
        assertThat(dto.getText()).isEqualTo("Test-Notiz");
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 5, 31, 8, 0));
        assertThat(dto.getCreatedBy()).isEqualTo("Max Mustermann");
    }

    @Test
    @DisplayName("toDto() gibt null zurück bei null-Eingabe")
    void toDto_shouldReturnNull_whenNoteIsNull() {
        // Act: Null übergeben
        NoteDto dto = noteMapper.toDto(null);

        // Assert: Rückgabewert muss null sein
        assertThat(dto).isNull();
    }
}

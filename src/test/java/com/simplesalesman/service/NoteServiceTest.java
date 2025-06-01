package com.simplesalesman.service;

import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Note;
import com.simplesalesman.mapper.NoteMapper;
import com.simplesalesman.repository.AddressRepository;
import com.simplesalesman.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    private NoteRepository noteRepository;
    private AddressRepository addressRepository;
    private NoteMapper noteMapper;
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        noteRepository = mock(NoteRepository.class);
        addressRepository = mock(AddressRepository.class);
        noteMapper = mock(NoteMapper.class);
        noteService = new NoteService(noteRepository, addressRepository, noteMapper);
    }

    @Test
    @DisplayName("getNotesForAddress gibt Liste von NoteDto zurück")
    void getNotesForAddress_returnsNoteDtos() {
        // Arrange: Adresse mit zwei Notizen simulieren
        Long addressId = 1L;
        Address address = new Address();
        address.setId(addressId);

        Note note1 = new Note();
        note1.setId(100L);
        Note note2 = new Note();
        note2.setId(101L);

        address.setNotes(Arrays.asList(note1, note2));

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(noteMapper.toDto(note1)).thenReturn(new NoteDto());
        when(noteMapper.toDto(note2)).thenReturn(new NoteDto());

        // Act
        List<NoteDto> result = noteService.getNotesForAddress(addressId);

        // Assert
        assertEquals(2, result.size());
        verify(addressRepository).findById(addressId);
        verify(noteMapper).toDto(note1);
        verify(noteMapper).toDto(note2);
    }

    @Test
    @DisplayName("getNotesForAddress wirft Exception, wenn Adresse nicht gefunden")
    void getNotesForAddress_throwsExceptionIfAddressNotFound() {
        // Arrange
        Long addressId = 1L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.getNotesForAddress(addressId));
        assertEquals("Adresse nicht gefunden", exception.getMessage());
    }

    @Test
    @DisplayName("addNoteToAddress speichert Note korrekt")
    void addNoteToAddress_savesNote() {
        // Arrange
        Long addressId = 1L;
        String text = "Testnote";
        String createdBy = "tester";

        Address address = new Address();
        address.setId(addressId);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        // Act
        noteService.addNoteToAddress(addressId, text, createdBy);

        // Assert: Prüfen, ob NoteRepository.save aufgerufen wurde
        verify(noteRepository).save(argThat(note ->
                note.getText().equals(text)
                && note.getCreatedBy().equals(createdBy)
                && note.getAddress().equals(address)
                && note.getCreatedAt() != null
        ));
    }

    @Test
    @DisplayName("addNoteToAddress wirft Exception, wenn Adresse nicht gefunden")
    void addNoteToAddress_throwsExceptionIfAddressNotFound() {
        // Arrange
        Long addressId = 1L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.addNoteToAddress(addressId, "Text", "user"));
        assertEquals("Adresse nicht gefunden", exception.getMessage());
    }
}

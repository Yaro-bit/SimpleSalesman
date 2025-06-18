package com.simplesalesman.service;

import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Note;
import com.simplesalesman.mapper.NoteMapper;
import com.simplesalesman.repository.AddressRepository;
import com.simplesalesman.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing notes linked to addresses in the SimpleSalesman application.
 *
 * This service handles creation, update, retrieval, and deletion of {@link Note} entities
 * that are associated with an {@link Address}.
 *
 * Use Cases:
 * - Sales reps add notes to addresses during or after D2D visits
 * - Admins or users update existing note texts
 * - Notes can be retrieved per address for display in UI
 *
 * Dependencies:
 * - {@link NoteRepository} for persistence
 * - {@link AddressRepository} to ensure valid address relations
 * - {@link NoteMapper} for entity-DTO conversion
 *
 * Error Handling:
 * - Uses {@link RuntimeException} with meaningful messages
 * - Assumes validation is handled at controller or DTO level
 *
 * Security Considerations:
 * - Only valid and existing address/note IDs should be passed
 * - User identity (`createdBy`) should be verified upstream (e.g. via JWT)
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.3
 */
@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final AddressRepository addressRepository;
    private final NoteMapper noteMapper;

    public NoteService(NoteRepository noteRepository, AddressRepository addressRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.addressRepository = addressRepository;
        this.noteMapper = noteMapper;
    }

    /**
     * Retrieves all notes linked to a specific address.
     *
     * @param addressId the ID of the address
     * @return list of NoteDto objects for frontend display
     * @throws RuntimeException if address is not found
     */
    public List<NoteDto> getNotesForAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Adresse nicht gefunden"));

        return address.getNotes().stream()
                .map(noteMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Adds a new note to a given address.
     *
     * @param addressId the target address ID
     * @param text the content of the note
     * @param createdBy name or identifier of the creator (e.g., from JWT token)
     * @throws RuntimeException if address is not found
     */
    public void addNoteToAddress(Long addressId, String text, String createdBy) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Adresse nicht gefunden"));

        Note note = new Note();
        note.setText(text);
        note.setCreatedAt(LocalDateTime.now());
        note.setCreatedBy(createdBy);
        note.setAddress(address);

        noteRepository.save(note);
    }

    /**
     * Updates the text of an existing note.
     *
     * @param noteId the ID of the note to update
     * @param newText new note content
     * @throws RuntimeException if note is not found
     */
    public void updateNoteText(Long noteId, String newText) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Notiz nicht gefunden"));
        note.setText(newText);
        noteRepository.save(note);
    }

    /**
     * Deletes a note by its ID.
     *
     * @param noteId ID of the note to delete
     * @throws RuntimeException if note does not exist
     */
    public void deleteNoteById(Long noteId) {
        if (!noteRepository.existsById(noteId)) {
            throw new RuntimeException("Notiz nicht gefunden");
        }
        noteRepository.deleteById(noteId);
    }

}

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
     * Retrieves all notes in the system with their associated address information.
     * 
     * @return list of all NoteDto objects
     */
    public List<NoteDto> getAllNotes() {
        List<Note> allNotes = noteRepository.findAll();
        return allNotes.stream()
                .map(noteMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all notes linked to a specific address.
     *
     * @param addressId the ID of the address
     * @return list of NoteDto objects for frontend display
     */
    public List<NoteDto> getNotesForAddress(Long addressId) {
        // Direct query - only 1 database hit instead of N+1
        List<Note> notes = noteRepository.findByAddressId(addressId);
        
        return notes.stream()
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
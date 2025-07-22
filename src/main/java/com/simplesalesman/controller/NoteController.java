package com.simplesalesman.controller;

import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing notes associated with addresses in the SimpleSalesman application.
 *
 * This controller provides endpoints to:
 * - Retrieve all notes in the system
 * - Retrieve all notes linked to a specific address
 * - Add a new note to an address
 * - Update an existing note
 * - Delete a note
 *
 * Logging and validation are included to ensure traceability and stability.
 * This is part of the core feature set used by sales personnel to track D2D visits.
 *
 * Security Considerations:
 * - Only valid input is accepted
 * - No free-form parameters in the URL
 * - Input is validated on all endpoints
 *
 * API Endpoints:
 * - GET /api/v1/notes - Get all notes
 * - GET /api/v1/notes/{addressId} - Get notes for specific address
 * - POST /api/v1/notes/{addressId} - Add note to address
 * - PUT /api/v1/notes/{noteId} - Update note
 * - DELETE /api/v1/notes/{noteId} - Delete note
 *
 * @author SimpleSalesman Team
 * @version 0.1.0
 * @since 0.0.4
 */
@RestController
@RequestMapping("/api/v1/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
    private final NoteService noteService;

    /**
     * Constructor for NoteController.
     *
     * @param noteService Service component handling business logic for notes
     */
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
        logger.info("NoteController initialized");
    }

    /**
     * Returns all notes in the system.
     * 
     * @return List of all NoteDto objects with address information
     */
    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes() {
        logger.info("GET request received for all notes");
        List<NoteDto> notes = noteService.getAllNotes();
        logger.debug("Found {} total notes", notes.size());
        return ResponseEntity.ok(notes);
    }

    /**
     * Returns a list of notes for the given address.
     *
     * @param addressId ID of the address
     * @return List of NoteDto objects
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<List<NoteDto>> getNotesForAddress(@PathVariable Long addressId) {
        logger.info("GET request received for notes of address ID {}", addressId);
        List<NoteDto> notes = noteService.getNotesForAddress(addressId);
        logger.debug("Found {} notes for address ID {}", notes.size(), addressId);
        return ResponseEntity.ok(notes);
    }

    /**
     * Adds a new note to the specified address.
     *
     * @param addressId ID of the address
     * @param payload Map containing "text" and "createdBy"
     * @return Response message indicating success
     */
    @PostMapping("/{addressId}")
    public ResponseEntity<Map<String, String>> addNoteToAddress(
            @PathVariable Long addressId,
            @RequestBody Map<String, String> payload) {

        String text = payload.get("text");
        String createdBy = payload.get("createdBy");

        if (text == null || text.trim().isEmpty()) {
            logger.warn("Empty note text received for address ID {}", addressId);
            return ResponseEntity.badRequest().body(Map.of("error", "Note text must not be empty"));
        }

        noteService.addNoteToAddress(addressId, text, createdBy);
        return ResponseEntity.ok(Map.of("message", "Note saved successfully"));
    }

    /**
     * Updates the text of an existing note.
     *
     * @param noteId ID of the note
     * @param payload Map containing new "text"
     * @return Response message indicating success
     */
    @PutMapping("/{noteId}")
    public ResponseEntity<Map<String, String>> updateNote(
            @PathVariable Long noteId,
            @RequestBody Map<String, String> payload) {

        String newText = payload.get("text");

        if (newText == null || newText.trim().isEmpty()) {
            logger.warn("Empty update text received for note ID {}", noteId);
            return ResponseEntity.badRequest().body(Map.of("error", "Note text must not be empty"));
        }

        try {
            noteService.updateNoteText(noteId, newText);
            logger.debug("Note ID {} successfully updated", noteId);
            return ResponseEntity.ok(Map.of("message", "Note updated successfully"));
        } catch (IllegalArgumentException e) {
            logger.warn("Note ID {} not found or invalid: {}", noteId, e.getMessage());
            return ResponseEntity.status(404).body(Map.of("error", "Note not found"));
        } catch (Exception e) {
            logger.error("Unexpected error updating note ID {}: {}", noteId, e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Deletes a note by its ID.
     *
     * @param noteId ID of the note
     * @return Response message indicating success
     */
    @DeleteMapping("/{noteId}")
    public ResponseEntity<Map<String, String>> deleteNote(@PathVariable Long noteId) {
        logger.info("DELETE request to remove note ID {}", noteId);

        try {
            noteService.deleteNoteById(noteId);
            logger.debug("Note ID {} successfully deleted", noteId);
            return ResponseEntity.ok(Map.of("message", "Note deleted successfully"));
        } catch (IllegalArgumentException e) {
            logger.warn("Note ID {} not found or invalid: {}", noteId, e.getMessage());
            return ResponseEntity.status(404).body(Map.of("error", "Note not found"));
        } catch (Exception e) {
            logger.error("Unexpected error deleting note ID {}: {}", noteId, e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
}
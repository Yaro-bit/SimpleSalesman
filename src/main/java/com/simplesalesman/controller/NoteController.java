package com.simplesalesman.controller;

import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notes")
@CrossOrigin(origins = "*")
public class NoteController {

	private final NoteService noteService;

	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<List<NoteDto>> getNotesForAddress(@PathVariable Long addressId) {
		return ResponseEntity.ok(noteService.getNotesForAddress(addressId));
	}

	@PostMapping("/{addressId}")
	public ResponseEntity<Map<String, String>> addNoteToAddress(@PathVariable Long addressId,
			@RequestBody Map<String, String> payload) {
		String text = payload.get("text");
		String createdBy = payload.get("createdBy");
		noteService.addNoteToAddress(addressId, text, createdBy);

		return ResponseEntity.ok(Map.of("message", "Notiz erfolgreich gespeichert"));
	}

	@PutMapping("/{noteId}")
	public ResponseEntity<Map<String, String>> updateNote(@PathVariable Long noteId,
			@RequestBody Map<String, String> payload) {
		String newText = payload.get("text");
		noteService.updateNoteText(noteId, newText);
		return ResponseEntity.ok(Map.of("message", "Notiz erfolgreich aktualisiert"));
	}
	
	@DeleteMapping("/{noteId}")
	public ResponseEntity<Map<String, String>> deleteNote(@PathVariable Long noteId) {
	    noteService.deleteNoteById(noteId);
	    return ResponseEntity.ok(Map.of("message", "Notiz erfolgreich gelöscht"));
	}


}

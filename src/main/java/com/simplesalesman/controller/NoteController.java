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
    public ResponseEntity<Void> addNoteToAddress(@PathVariable Long addressId,
                                                 @RequestBody Map<String, String> payload) {
        String text = payload.get("text");
        String createdBy = payload.get("createdBy");
        noteService.addNoteToAddress(addressId, text, createdBy);
        return ResponseEntity.ok().build();
    }
}

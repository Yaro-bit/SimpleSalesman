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

    public List<NoteDto> getNotesForAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Adresse nicht gefunden"));

        return address.getNotes().stream()
                .map(noteMapper::toDto)
                .collect(Collectors.toList());
    }

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

    // Optional: Notizen löschen/bearbeiten
}

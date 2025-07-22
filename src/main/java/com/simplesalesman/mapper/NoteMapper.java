package com.simplesalesman.mapper;

import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.entity.Note;
import org.springframework.stereotype.Component;

/**
 * Mapper für Note <-> NoteDto. Achtung: toEntity() mapped nur Basisfelder (kein
 * Address-Bezug). Für Zuordnung zur Address zuständiger Service nötig.
 *
 * @author SimpleSalesman Team
 * @version 0.1.0
 * @since 0.0.3
 */
@Component
public class NoteMapper {

    public NoteDto toDto(Note note) {
        if (note == null)
            return null;

        NoteDto dto = new NoteDto();
        dto.setId(note.getId());
        dto.setText(note.getText());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setCreatedBy(note.getCreatedBy());
        
        // Map address information if available
        if (note.getAddress() != null) {
            dto.setAddressId(note.getAddress().getId());
            dto.setAddressText(note.getAddress().getAddressText());
        }
        
        return dto;
    }

    public Note toEntity(NoteDto dto) {
        if (dto == null)
            return null;

        Note note = new Note();
        note.setId(dto.getId());
        note.setText(dto.getText()); 
        // createdAt und createdBy werden meist automatisch gesetzt (z. B. Audit)
        // Address must be set by service layer based on addressId
        return note;
    }
}
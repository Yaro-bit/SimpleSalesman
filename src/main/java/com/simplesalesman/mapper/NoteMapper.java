package com.simplesalesman.mapper;

import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.entity.Note;
import org.springframework.stereotype.Component;

/**
 * Mapper für Note <-> NoteDto. Achtung: toEntity() mapped nur Basisfelder (kein
 * Address-Bezug). Für Zuordnung zur Address zuständiger Service nötig.
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
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
		return dto;
	}

	public Note toEntity(NoteDto dto) {
		if (dto == null)
			return null;

		Note note = new Note();
		note.setId(dto.getId());
		note.setText(dto.getText()); // createdAt und createdBy werden meist automatisch gesetzt (z. B. Audit)
		return note;
	}
}

package com.simplesalesman.mapper;

import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

	public NoteDto toDto(Note note) {
	    if (note == null) {
	        return null;
	    }
	    NoteDto dto = new NoteDto();
	    dto.setId(note.getId());
	    dto.setText(note.getText());
	    dto.setCreatedAt(note.getCreatedAt());
	    dto.setCreatedBy(note.getCreatedBy());
	    return dto;
	}

    // Optional: toEntity(NoteDto dto)
    
}

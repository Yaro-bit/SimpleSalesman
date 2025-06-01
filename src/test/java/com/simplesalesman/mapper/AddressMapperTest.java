package com.simplesalesman.mapper;

import com.simplesalesman.dto.AddressDto;
import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Note;
import com.simplesalesman.entity.Project;
import com.simplesalesman.entity.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AddressMapperTest {

    private ProjectMapper projectMapper;
    private NoteMapper noteMapper;
    private AddressMapper addressMapper;

    @BeforeEach
    void setUp() {
        projectMapper = mock(ProjectMapper.class);
        noteMapper = mock(NoteMapper.class);
        addressMapper = new AddressMapper(projectMapper, noteMapper);
    }

    @Test
    void toDto_shouldMapAllFieldsCorrectly() {
        // Arrange: Entity anlegen
        Address address = new Address();
        address.setId(1L);
        address.setAddressText("Musterstraße 1");
        Region region = new Region();
        region.setName("Wien");
        address.setRegion(region);

        Project project = new Project();
        Note note = new Note();
        address.setProjects(List.of(project));
        address.setNotes(List.of(note));

        // Dummy-Dtos vorbereiten (werden von den gemockten Mappern geliefert)
        ProjectDto projectDto = new ProjectDto();
        NoteDto noteDto = new NoteDto();

        when(projectMapper.toDto(project)).thenReturn(projectDto);
        when(noteMapper.toDto(note)).thenReturn(noteDto);

        // Act
        AddressDto dto = addressMapper.toDto(address);

        // Assert: Felder korrekt gemappt?
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getAddressText()).isEqualTo("Musterstraße 1");
        assertThat(dto.getRegionName()).isEqualTo("Wien");
        assertThat(dto.getProjects()).containsExactly(projectDto);
        assertThat(dto.getNotes()).containsExactly(noteDto);

        // Sicherstellen, dass Mapper aufgerufen wurden
        verify(projectMapper).toDto(project);
        verify(noteMapper).toDto(note);
    }

    @Test
    void toDto_shouldSetRegionNameNullIfNoRegion() {
        Address address = new Address();
        address.setId(2L);
        address.setAddressText("Ohne Region");
        address.setRegion(null);
        address.setProjects(Collections.emptyList());
        address.setNotes(Collections.emptyList());

        AddressDto dto = addressMapper.toDto(address);

        assertThat(dto.getRegionName()).isNull();
    }
}

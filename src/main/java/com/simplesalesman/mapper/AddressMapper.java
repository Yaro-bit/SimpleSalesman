package com.simplesalesman.mapper;

import com.simplesalesman.dto.AddressDto;
import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Project;
import com.simplesalesman.entity.Note;
import com.simplesalesman.entity.Region;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper für Address <-> AddressDto.
 * Achtung: toEntity() mapped nur Basisfelder!
 * Für Region, Projekte, Notizen: Lookup im Service nötig.
 *
 * @author SimpleSalesman Team
 * @version 0.1.0
 * @since 0.0.3
 */
@Component
public class AddressMapper {

    private final ProjectMapper projectMapper;
    private final NoteMapper noteMapper;

    public AddressMapper(ProjectMapper projectMapper, NoteMapper noteMapper) {
        this.projectMapper = projectMapper;
        this.noteMapper = noteMapper;
    }

    public AddressDto toDto(Address address) {
        if (address == null) return null;

        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setAddressText(address.getAddressText());
        dto.setRegionName(address.getRegion() != null ? address.getRegion().getName() : null);
        dto.setProjects(
                address.getProjects() != null
                        ? address.getProjects().stream().map(projectMapper::toDto).collect(Collectors.toList())
                        : null
        );
        dto.setNotes(
                address.getNotes() != null
                        ? address.getNotes().stream().map(noteMapper::toDto).collect(Collectors.toList())
                        : null
        );
        return dto;
    }

    public Address toEntity(AddressDto dto) {
        if (dto == null) return null;

        Address address = new Address();
        address.setId(dto.getId());
        address.setAddressText(dto.getAddressText());

        // Handling Region: You would need to look up the Region based on the region name (this may need to be done in the service)
        // This is just an example, the actual lookup should happen in the service layer.
        if (dto.getRegionName() != null) {
            Region region = new Region(); // Assuming Region lookup happens elsewhere (e.g., in service)
            region.setName(dto.getRegionName());
            address.setRegion(region);
        }

        // Handling Projects
        if (dto.getProjects() != null) {
            address.setProjects(dto.getProjects().stream()
                    .map(projectMapper::toEntity) // Convert ProjectDto to Project
                    .collect(Collectors.toSet())); // Set<Project> is expected in Address entity
        }

        // Handling Notes
        if (dto.getNotes() != null) {
            address.setNotes(dto.getNotes().stream()
                    .map(noteMapper::toEntity) // Convert NoteDto to Note
                    .collect(Collectors.toSet())); // Set<Note> is expected in Address entity
        }

        return address;
    }
}

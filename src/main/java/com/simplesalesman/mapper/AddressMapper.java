package com.simplesalesman.mapper;

import com.simplesalesman.dto.AddressDto;
import com.simplesalesman.entity.Address;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper für Address <-> AddressDto.
 * Achtung: toEntity() mapped nur Basisfelder!
 * Für Region, Projekte, Notizen: Lookup im Service nötig.
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
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
        return address;
    }
}

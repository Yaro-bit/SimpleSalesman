package com.simplesalesman.service;

import com.simplesalesman.dto.AddressDto;
import com.simplesalesman.dto.ProjectDto;
import com.simplesalesman.dto.NoteDto;
import com.simplesalesman.entity.Address;
import com.simplesalesman.entity.Project;
import com.simplesalesman.entity.Note;
import com.simplesalesman.entity.Region;
import com.simplesalesman.mapper.AddressMapper;
import com.simplesalesman.repository.AddressRepository;
import com.simplesalesman.repository.RegionRepository;
import com.simplesalesman.repository.ProjectRepository;
import com.simplesalesman.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing address-related operations in the SimpleSalesman application.
 *
 * @author SimpleSalesman Team
 * @version 0.1.0
 * @since 0.0.3
 */
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final RegionRepository regionRepository;
    private final ProjectRepository projectRepository;
    private final NoteRepository noteRepository;

    public AddressService(AddressRepository addressRepository,
                         AddressMapper addressMapper,
                         RegionRepository regionRepository,
                         ProjectRepository projectRepository,
                         NoteRepository noteRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.regionRepository = regionRepository;
        this.projectRepository = projectRepository;
        this.noteRepository = noteRepository;
    }

    /**
     * Retrieves all addresses from the database.
     *
     * @return list of all addresses as DTOs
     */
    public List<AddressDto> getAllAddresses() {
        return addressRepository.findAllWithNotesProjectsAndRegion().stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single address by its ID.
     *
     * @param id the address ID
     * @return the corresponding AddressDto or null if not found
     */
    public AddressDto getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toDto)
                .orElse(null);
    }

    /**
     * Creates a new address with linked region, notes, and projects.
     *
     * @param dto the input AddressDto
     * @return the saved AddressDto
     */
    public AddressDto createAddress(AddressDto dto) {
        Address address = addressMapper.toEntity(dto);

        setRegion(dto.getRegionName(), address);
        setProjects(dto.getProjects(), address);
        setNotes(dto.getNotes(), address);

        Address saved = addressRepository.save(address);
        return addressMapper.toDto(saved);
    }

    /**
     * Updates an existing address and its associated region, notes, and projects.
     *
     * @param id  the ID of the address to update
     * @param dto the updated AddressDto
     * @return the updated AddressDto
     */
    public AddressDto updateAddress(Long id, AddressDto dto) {
        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adresse nicht gefunden"));

        existing.setAddressText(dto.getAddressText());

        setRegion(dto.getRegionName(), existing);
        setProjects(dto.getProjects(), existing);
        setNotes(dto.getNotes(), existing);

        Address saved = addressRepository.save(existing);
        return addressMapper.toDto(saved);
    }

    /**
     * Deletes an address by ID if it exists.
     *
     * @param id the ID of the address to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Helper method to set the region for an address.
     */
    private void setRegion(String regionName, Address address) {
        if (regionName != null) {
            Region region = regionRepository.findByName(regionName)
                    .orElseThrow(() -> new RuntimeException("Region nicht gefunden: " + regionName));
            address.setRegion(region);
        }
    }

    /**
     * Helper method to set the projects for an address.
     */
    private void setProjects(List<ProjectDto> projectsDto, Address address) {
        if (projectsDto != null && !projectsDto.isEmpty()) {
            Set<Project> projects = projectsDto.stream()
                    .map(projectDto -> projectRepository.findById(projectDto.getId())
                            .orElseThrow(() -> new RuntimeException("Projekt nicht gefunden: " + projectDto.getId())))
                    .collect(Collectors.toSet());
            address.setProjects(projects);
        }
    }

    /**
     * Helper method to set the notes for an address.
     */
    private void setNotes(List<NoteDto> notesDto, Address address) {
        if (notesDto != null && !notesDto.isEmpty()) {
            Set<Note> notes = notesDto.stream()
                    .map(noteDto -> noteRepository.findById(noteDto.getId())
                            .orElseThrow(() -> new RuntimeException("Notiz nicht gefunden: " + noteDto.getId())))
                    .collect(Collectors.toSet());
            address.setNotes(notes);
        }
    }
}

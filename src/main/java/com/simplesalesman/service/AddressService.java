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
import java.util.stream.Collectors;

/**
 * Service class for managing address-related operations in the SimpleSalesman application.
 *
 * This service handles the core business logic for address entities, including:
 * - Retrieving all addresses
 * - Fetching a single address by ID
 * - Creating a new address with associated region, notes, and projects
 * - Updating an existing address and its relations
 * - Deleting an address by ID
 *
 * Dependencies:
 * - {@link AddressRepository} for persistence
 * - {@link AddressMapper} for DTO conversion
 * - {@link RegionRepository}, {@link ProjectRepository}, {@link NoteRepository} for linked data resolution
 *
 * Used by:
 * - {@code AddressController}
 *
 * Security Considerations:
 * - Input validation should occur at controller or DTO level
 * - Errors are propagated as RuntimeExceptions for centralized handling
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
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
        return addressRepository.findAll().stream()
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

        if (dto.getRegionName() != null) {
            Region region = regionRepository.findByName(dto.getRegionName())
                    .orElseThrow(() -> new RuntimeException("Region nicht gefunden: " + dto.getRegionName()));
            address.setRegion(region);
        }

        if (dto.getProjects() != null && !dto.getProjects().isEmpty()) {
            List<Project> projects = dto.getProjects().stream()
                    .map(projectDto -> projectRepository.findById(projectDto.getId())
                            .orElseThrow(() -> new RuntimeException("Projekt nicht gefunden: " + projectDto.getId())))
                    .collect(Collectors.toList());
            address.setProjects(projects);
        }

        if (dto.getNotes() != null && !dto.getNotes().isEmpty()) {
            List<Note> notes = dto.getNotes().stream()
                    .map(noteDto -> noteRepository.findById(noteDto.getId())
                            .orElseThrow(() -> new RuntimeException("Notiz nicht gefunden: " + noteDto.getId())))
                    .collect(Collectors.toList());
            address.setNotes(notes);
        }

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

        if (dto.getRegionName() != null) {
            Region region = regionRepository.findByName(dto.getRegionName())
                    .orElseThrow(() -> new RuntimeException("Region nicht gefunden: " + dto.getRegionName()));
            existing.setRegion(region);
        }

        if (dto.getProjects() != null && !dto.getProjects().isEmpty()) {
            List<Project> projects = dto.getProjects().stream()
                    .map(projectDto -> projectRepository.findById(projectDto.getId())
                            .orElseThrow(() -> new RuntimeException("Projekt nicht gefunden: " + projectDto.getId())))
                    .collect(Collectors.toList());
            existing.setProjects(projects);
        }

        if (dto.getNotes() != null && !dto.getNotes().isEmpty()) {
            List<Note> notes = dto.getNotes().stream()
                    .map(noteDto -> noteRepository.findById(noteDto.getId())
                            .orElseThrow(() -> new RuntimeException("Notiz nicht gefunden: " + noteDto.getId())))
                    .collect(Collectors.toList());
            existing.setNotes(notes);
        }

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
}

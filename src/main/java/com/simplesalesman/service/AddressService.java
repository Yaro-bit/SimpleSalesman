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

    public List<AddressDto> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    public AddressDto getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Adresse nicht gefunden"));
    }

    public AddressDto createAddress(AddressDto dto) {
        Address address = addressMapper.toEntity(dto);

        // Region zuweisen, falls vorhanden
        if (dto.getRegionName() != null) {
            Region region = regionRepository.findByName(dto.getRegionName())
                    .orElseThrow(() -> new RuntimeException("Region nicht gefunden: " + dto.getRegionName()));
            address.setRegion(region);
        }

        // Projekte zuweisen, falls vorhanden
        if (dto.getProjects() != null && !dto.getProjects().isEmpty()) {
            List<Project> projects = dto.getProjects().stream()
                    .map(projectDto -> projectRepository.findById(projectDto.getId())
                            .orElseThrow(() -> new RuntimeException("Projekt nicht gefunden: " + projectDto.getId())))
                    .collect(Collectors.toList());
            address.setProjects(projects);
        }

        // Notizen zuweisen, falls vorhanden
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

    public AddressDto updateAddress(Long id, AddressDto dto) {
        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adresse nicht gefunden"));

        existing.setAddressText(dto.getAddressText());

        // Region ggf. ändern
        if (dto.getRegionName() != null) {
            Region region = regionRepository.findByName(dto.getRegionName())
                    .orElseThrow(() -> new RuntimeException("Region nicht gefunden: " + dto.getRegionName()));
            existing.setRegion(region);
        }

        // Projekte ggf. ändern
        if (dto.getProjects() != null && !dto.getProjects().isEmpty()) {
            List<Project> projects = dto.getProjects().stream()
                    .map(projectDto -> projectRepository.findById(projectDto.getId())
                            .orElseThrow(() -> new RuntimeException("Projekt nicht gefunden: " + projectDto.getId())))
                    .collect(Collectors.toList());
            existing.setProjects(projects);
        }

        // Notizen ggf. ändern
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

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}

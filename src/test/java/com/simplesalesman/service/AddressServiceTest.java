package com.simplesalesman.service;

import com.simplesalesman.dto.AddressDto;
import com.simplesalesman.entity.Address;
import com.simplesalesman.mapper.AddressMapper;
import com.simplesalesman.repository.AddressRepository;
import com.simplesalesman.repository.RegionRepository;
import com.simplesalesman.repository.ProjectRepository;
import com.simplesalesman.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests für AddressService.
 * Es werden alle CRUD-Methoden abgedeckt.
 * Die Abhängigkeiten (Repos, Mapper) werden mit Mockito gemockt.
 */
class AddressServiceTest {

    // Mocked Dependencies für AddressService
    private AddressRepository addressRepository;
    private AddressMapper addressMapper;
    private RegionRepository regionRepository;
    private ProjectRepository projectRepository;
    private NoteRepository noteRepository;

    // System Under Test
    private AddressService addressService;

    @BeforeEach
    void setUp() {
        // Erzeuge Mocks für alle benötigten Abhängigkeiten
        addressRepository = mock(AddressRepository.class);
        addressMapper = mock(AddressMapper.class);
        regionRepository = mock(RegionRepository.class);
        projectRepository = mock(ProjectRepository.class);
        noteRepository = mock(NoteRepository.class);

        // Service mit gemockten Dependencies initialisieren
        addressService = new AddressService(
            addressRepository,
            addressMapper,
            regionRepository,
            projectRepository,
            noteRepository
        );
    }

    @Test
    @DisplayName("getAllAddresses gibt gemappte AddressDto-Liste zurück")
    void getAllAddresses_returnsMappedDtos() {
        // Arrange: Zwei Dummy-Adressen und ihre DTOs vorbereiten
        Address address1 = new Address();
        Address address2 = new Address();
        AddressDto dto1 = new AddressDto();
        AddressDto dto2 = new AddressDto();

        List<Address> addressList = Arrays.asList(address1, address2);
        when(addressRepository.findAll()).thenReturn(addressList);
        when(addressMapper.toDto(address1)).thenReturn(dto1);
        when(addressMapper.toDto(address2)).thenReturn(dto2);

        // Act: Service-Methode ausführen
        List<AddressDto> result = addressService.getAllAddresses();

        // Assert: Prüfen, dass beide DTOs im Ergebnis enthalten sind
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        verify(addressRepository).findAll();
        verify(addressMapper, times(2)).toDto(any(Address.class));
    }

    @Test
    @DisplayName("getAllAddresses gibt leere Liste zurück, wenn keine Adressen vorhanden sind")
    void getAllAddresses_returnsEmptyList() {
        // Arrange: Repository liefert leere Liste
        when(addressRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<AddressDto> result = addressService.getAllAddresses();

        // Assert: Ergebnis ist nicht null und leer
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressRepository).findAll();
        verify(addressMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("getAddressById gibt gemapptes AddressDto für gefundene Adresse zurück")
    void getAddressById_returnsDto() {
        // Arrange: Adresse im Repository vorhanden
        Address address = new Address();
        AddressDto dto = new AddressDto();
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressMapper.toDto(address)).thenReturn(dto);

        // Act
        AddressDto result = addressService.getAddressById(1L);

        // Assert: Das richtige DTO wird zurückgegeben
        assertNotNull(result);
        assertEquals(dto, result);
        verify(addressRepository).findById(1L);
        verify(addressMapper).toDto(address);
    }

    @Test
    @DisplayName("getAddressById wirft Exception, wenn Adresse nicht gefunden wird")
    void getAddressById_notFound_throwsException() {
        // Arrange: Adresse nicht vorhanden
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert: Exception wird erwartet
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            addressService.getAddressById(1L);
        });

        assertTrue(ex.getMessage().contains("Adresse nicht gefunden"));
        verify(addressRepository).findById(1L);
        verify(addressMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("createAddress speichert Adresse und gibt gemapptes AddressDto zurück")
    void createAddress_savesAndReturnsDto() {
        // Arrange: Adress-DTO wird zu Entity gemappt, gespeichert und wieder gemappt
        AddressDto dto = new AddressDto();
        Address address = new Address();
        Address savedAddress = new Address();
        AddressDto savedDto = new AddressDto();

        when(addressMapper.toEntity(dto)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(savedAddress);
        when(addressMapper.toDto(savedAddress)).thenReturn(savedDto);

        // Act
        AddressDto result = addressService.createAddress(dto);

        // Assert: Richtiger Aufruf aller Mocks und Rückgabe des gemappten DTOs
        assertEquals(savedDto, result);
        verify(addressMapper).toEntity(dto);
        verify(addressRepository).save(address);
        verify(addressMapper).toDto(savedAddress);
    }

    @Test
    @DisplayName("updateAddress aktualisiert Adresse und gibt gemapptes AddressDto zurück")
    void updateAddress_updatesAndReturnsDto() {
        // Arrange: Adresse existiert, Felder werden aktualisiert und gespeichert
        Long id = 1L;
        AddressDto dto = new AddressDto();
        dto.setAddressText("Updated Address");
        Address existing = new Address();
        existing.setAddressText("Old Address");
        Address saved = new Address();
        AddressDto savedDto = new AddressDto();

        when(addressRepository.findById(id)).thenReturn(Optional.of(existing));
        when(addressRepository.save(existing)).thenReturn(saved);
        when(addressMapper.toDto(saved)).thenReturn(savedDto);

        // Act
        AddressDto result = addressService.updateAddress(id, dto);

        // Assert: AddressText wurde gesetzt und Mocks korrekt verwendet
        assertEquals(savedDto, result);
        assertEquals("Updated Address", existing.getAddressText());
        verify(addressRepository).findById(id);
        verify(addressRepository).save(existing);
        verify(addressMapper).toDto(saved);
    }

    @Test
    @DisplayName("updateAddress wirft Exception, wenn Adresse nicht gefunden wird")
    void updateAddress_notFound_throwsException() {
        // Arrange: Adresse nicht vorhanden
        Long id = 1L;
        AddressDto dto = new AddressDto();

        when(addressRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert: Exception wird erwartet
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            addressService.updateAddress(id, dto);
        });

        assertTrue(ex.getMessage().contains("Adresse nicht gefunden"));
        verify(addressRepository).findById(id);
        verify(addressRepository, never()).save(any());
        verify(addressMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("deleteAddress löscht die Adresse anhand der ID")
    void deleteAddress_deletesById() {
        // Arrange: Nur Aufruf erwartet

        // Act
        addressService.deleteAddress(1L);

        // Assert: deleteById wurde mit richtiger ID aufgerufen
        verify(addressRepository).deleteById(1L);
    }
}

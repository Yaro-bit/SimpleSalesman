package com.simplesalesman.controller;

import com.simplesalesman.dto.AddressDto;
import com.simplesalesman.service.AddressService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private AddressService addressService;

    @Test
    @WithMockUser // <<< und auch HIER!
    void getAllAddresses_returnsOkAndAddressList() throws Exception {
        // Arrange: Erzeuge Dummy-Daten mit den Feldern, die im DTO existieren
        AddressDto address1 = new AddressDto();
        address1.setId(1L);
        address1.setAddressText("Musterstraße 1, 1010 Wien");
        address1.setRegionName("Wien Mitte");

        AddressDto address2 = new AddressDto();
        address2.setId(2L);
        address2.setAddressText("Testweg 2, 1020 Wien");
        address2.setRegionName("Wien Nord");

        List<AddressDto> addresses = Arrays.asList(address1, address2);

        // Service mocken
        Mockito.when(addressService.getAllAddresses()).thenReturn(addresses);

        // Act & Assert
        mockMvc.perform(get("/api/v1/addresses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].addressText").value("Musterstraße 1, 1010 Wien"))
                .andExpect(jsonPath("$[0].regionName").value("Wien Mitte"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].addressText").value("Testweg 2, 1020 Wien"))
                .andExpect(jsonPath("$[1].regionName").value("Wien Nord"));
    }

    @Test
    @WithMockUser 
    void getAddress_returnsOkAndSingleAddress() throws Exception {
        // Arrange: Erzeuge Dummy-Daten mit den Feldern, die im DTO existieren
        AddressDto address = new AddressDto();
        address.setId(1L);
        address.setAddressText("Musterstraße 1, 1010 Wien");
        address.setRegionName("Wien Mitte");

        // Service mocken
        Mockito.when(addressService.getAddressById(anyLong())).thenReturn(address);

        // Act & Assert
        mockMvc.perform(get("/api/v1/addresses/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.addressText").value("Musterstraße 1, 1010 Wien"))
                .andExpect(jsonPath("$.regionName").value("Wien Mitte"));
    }
    
    @Test
    @WithMockUser
    void getAddress_returnsNotFound_whenAddressDoesNotExist() throws Exception {
        // Service so mocken, dass er keine Adresse liefert
        Mockito.when(addressService.getAddressById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/v1/addresses/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    
    
    
}

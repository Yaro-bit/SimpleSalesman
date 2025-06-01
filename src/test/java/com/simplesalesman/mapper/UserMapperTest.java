package com.simplesalesman.mapper;

import com.simplesalesman.dto.UserDto;
import com.simplesalesman.entity.AppUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    @DisplayName("toDto() should map all fields from AppUser to UserDto correctly")
    void toDto_shouldMapAllFields() {
        // Arrange: Erstelle einen Beispiel-User
        AppUser user = new AppUser();
        user.setId(1L);
        user.setKeycloakId("keycloak-123");
        user.setUsername("max.mustermann");
        user.setRole("USER");
        user.setActive(true);

        // Act: Führe die Mapping-Logik aus
        UserDto dto = userMapper.toDto(user);

        // Assert: Überprüfe, ob alle Felder korrekt übernommen wurden
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getKeycloakId()).isEqualTo("keycloak-123");
        assertThat(dto.getUsername()).isEqualTo("max.mustermann");
        assertThat(dto.getRole()).isEqualTo("USER");
        assertThat(dto.isActive()).isTrue();
    }

    @Test
    @DisplayName("toDto() should throw NullPointerException when input is null")
    void toDto_shouldThrowNullPointer_whenInputIsNull() {
        // Act + Assert
        // Wir erwarten eine NullPointerException, da der Mapper kein Null-Handling hat
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> userMapper.toDto(null));
    }
}

package com.simplesalesman.mapper;

import com.simplesalesman.dto.UserDto;
import com.simplesalesman.entity.AppUser;
import org.springframework.stereotype.Component;

/**
 * Mapper für AppUser <-> UserDto.
 * Achtung: toEntity() mapped nur Basisfelder, kein Keycloak-Token.
 * Wird v. a. für Auth-Profile und Admin-Übersicht genutzt.
 * 
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.4
 */
@Component
public class UserMapper {

    public UserDto toDto(AppUser user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setKeycloakId(user.getKeycloakId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }

    public AppUser toEntity(UserDto dto) {
        if (dto == null) return null;

        AppUser user = new AppUser();
        user.setId(dto.getId());
        user.setKeycloakId(dto.getKeycloakId());
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        user.setActive(dto.isActive());
        return user;
    }
}

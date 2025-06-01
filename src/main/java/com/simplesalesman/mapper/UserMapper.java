package com.simplesalesman.mapper;

import com.simplesalesman.dto.UserDto;
import com.simplesalesman.entity.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(AppUser user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setKeycloakId(user.getKeycloakId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }

    // Optional: toEntity(UserDto dto)
}

package com.simplesalesman.controller;

import com.simplesalesman.dto.UserDto;
import com.simplesalesman.entity.AppUser;
import com.simplesalesman.mapper.UserMapper;
import com.simplesalesman.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        AppUser user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}

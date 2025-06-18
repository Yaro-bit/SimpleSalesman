package com.simplesalesman.controller;

import com.simplesalesman.dto.UserDto;
import com.simplesalesman.entity.AppUser;
import com.simplesalesman.mapper.UserMapper;
import com.simplesalesman.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication and user profile operations in the SimpleSalesman application.
 *
 * This controller handles authentication-related endpoints, particularly for retrieving
 * the current authenticated user's profile information. It integrates with Keycloak
 * for JWT-based authentication and provides secure access to user data.
 *
 * Key features:
 * - JWT-based authentication with Keycloak integration
 * - Secure user profile retrieval
 * - Comprehensive error handling and logging
 * - Cross-origin support for web frontend integration
 * - Automatic user lookup by Keycloak ID
 *
 * API Endpoints:
 * - GET /api/v1/auth/me - Retrieve current authenticated user's profile
 *
 * Security:
 * - All endpoints require valid JWT authentication
 * - User identification through Keycloak subject claim
 * - Automatic user validation against internal database
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.4
 */
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*") //CHANGEIT For production, only allow specific domains
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Constructor for AuthController.
     *
     * @param userRepository Repository for user data access operations
     * @param userMapper Mapper for converting between User entities and DTOs
     */
    public AuthController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        logger.info("AuthController initialized");
    }

    /**
     * Retrieves the profile information of the currently authenticated user.
     *
     * This endpoint extracts the user's Keycloak ID from the JWT token,
     * looks up the corresponding user in the database, and returns their
     * profile information as a DTO.
     *
     * @param jwt The JWT token containing the authenticated user's information
     * @return ResponseEntity containing the UserDto with current user's profile
     *         Returns HTTP 200 (OK) with user profile data on success
     *         Returns HTTP 404 (Not Found) if user doesn't exist in database
     *         Returns HTTP 401 (Unauthorized) if JWT is invalid or missing
     *         Returns HTTP 500 (Internal Server Error) if an exception occurs
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        logger.info("GET request received for current user profile");
        
        try {
            // Extract Keycloak ID from JWT subject claim
            String keycloakId = jwt.getSubject();
            logger.debug("Processing authentication request for Keycloak ID: {}", keycloakId);
            
            // Look up user in database
            AppUser user = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> {
                        logger.warn("User not found in database for Keycloak ID: {}", keycloakId);
                        return new RuntimeException("Benutzer nicht gefunden");
                    });
            
            // Convert to DTO and return
            UserDto userDto = userMapper.toDto(user);
            logger.info("Successfully retrieved profile for user ID: {} (Keycloak ID: {})", 
                       user.getId(), keycloakId);
            
            return ResponseEntity.ok(userDto);
            
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Benutzer nicht gefunden")) {
                logger.error("User lookup failed: {}", e.getMessage());
                return ResponseEntity.notFound().build();
            } else {
                logger.error("Error processing authentication request", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            logger.error("Unexpected error during user profile retrieval", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
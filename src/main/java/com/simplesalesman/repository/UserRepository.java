package com.simplesalesman.repository;

import com.simplesalesman.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link AppUser} entities in the
 * SimpleSalesman application.
 *
 * This interface extends Spring Data JPA's {@link JpaRepository} and provides
 * full CRUD capabilities for accessing user data. It also includes a custom
 * query method to find users by their Keycloak ID.
 *
 * Use Cases: - Managing user accounts linked to Keycloak identities -
 * Retrieving user profiles for authentication and role assignment
 *
 * Custom Methods: -
 * {@code Optional<AppUser> findByKeycloakId(String keycloakId)}: Look up a user
 * via their Keycloak identifier
 *
 * Entity: {@link AppUser} ID Type: {@link Long}
 *
 * Example: - userRepository.findByKeycloakId("abc-123-def"); -
 * userRepository.save(new AppUser(...));
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.3
 */

public interface UserRepository extends JpaRepository<AppUser, Long> {
	Optional<AppUser> findByKeycloakId(String keycloakId);
}

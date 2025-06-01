package com.simplesalesman.repository;

import com.simplesalesman.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByKeycloakId(String keycloakId);
}

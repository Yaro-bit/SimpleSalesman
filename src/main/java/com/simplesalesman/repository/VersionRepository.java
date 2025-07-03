// 2. Repository Interface (VersionRepository.java)
package com.simplesalesman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simplesalesman.entity.Version;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
    // JpaRepository provides basic CRUD operations
}
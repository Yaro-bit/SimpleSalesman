// 2. Repository Interface (VersionRepository.java)
package com.simplesalesman.exam.version;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
    // JpaRepository provides basic CRUD operations
}
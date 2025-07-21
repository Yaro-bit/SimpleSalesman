package com.simplesalesman.repository;

import com.simplesalesman.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
/**
 * Repository interface for managing {@link Address} entities in the SimpleSalesman application.
 *
 * This interface extends Spring Data JPA's {@link JpaRepository}, providing CRUD operations
 * and query capabilities for Address objects persisted in the database.
 *
 * Usage:
 * - Inject this repository in services to access address data
 * - Supports pagination, sorting, and custom query definitions
 *
 * Entity: {@link Address}
 * ID Type: {@link Long}
 *
 * Example:
 * - addressRepository.findById(1L);
 * - addressRepository.save(new Address(...));
 *
 * Author: SimpleSalesman Team
 * @version 0.1.0
 * @since 0.0.3
 */


public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("""
        SELECT DISTINCT a FROM Address a
        LEFT JOIN FETCH a.notes
        LEFT JOIN FETCH a.projects
        LEFT JOIN FETCH a.region
    """)
    List<Address> findAllWithNotesProjectsAndRegion();
}
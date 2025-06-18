package com.simplesalesman.repository;

import com.simplesalesman.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
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
 * @version 0.0.6
 * @since 0.0.3
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
}

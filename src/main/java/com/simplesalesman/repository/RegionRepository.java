package com.simplesalesman.repository;

import com.simplesalesman.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Region} entities in the
 * SimpleSalesman application.
 *
 * This interface extends Spring Data JPA's {@link JpaRepository} and provides
 * database access for all region-related data, including custom queries such as
 * lookup by name.
 *
 * Use Cases: - Retrieving region metadata for addresses and weather queries -
 * Mapping addresses to regions during import or UI filtering
 *
 * Custom Methods: - {@code Optional<Region> findByName(String name)}: Find a
 * region by its name
 *
 * Entity: {@link Region} ID Type: {@link Long}
 *
 * Example: - regionRepository.findByName("Wels"); - regionRepository.save(new
 * Region(...));
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.3
 */
public interface RegionRepository extends JpaRepository<Region, Long> {
	Optional<Region> findByName(String name);
}

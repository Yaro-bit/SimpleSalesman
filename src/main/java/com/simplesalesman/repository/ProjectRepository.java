package com.simplesalesman.repository;

import com.simplesalesman.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Project} entities in the
 * SimpleSalesman application.
 *
 * This interface extends Spring Data JPA's {@link JpaRepository} and provides
 * full CRUD functionality for accessing and modifying project records in the
 * database.
 *
 * Use Cases: - Importing, retrieving, and updating construction or
 * sales-related projects - Linking addresses to specific projects for route
 * planning and filtering
 *
 * Entity: {@link Project} ID Type: {@link Long}
 *
 * Example: - projectRepository.findAll(); - projectRepository.save(new
 * Project(...));
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.3
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
}

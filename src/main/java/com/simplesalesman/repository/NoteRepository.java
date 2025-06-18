package com.simplesalesman.repository;

import com.simplesalesman.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Note} entities in the SimpleSalesman
 * application.
 *
 * This interface provides basic CRUD operations for note entries stored in the
 * database. It extends Spring Data JPA's {@link JpaRepository}, enabling
 * efficient persistence and retrieval.
 *
 * Typical Use Cases: - Storing new notes related to an address - Fetching,
 * updating, or deleting notes linked to D2D visits
 *
 * Entity: {@link Note} ID Type: {@link Long}
 *
 * Example: - noteRepository.findById(42L); - noteRepository.save(new
 * Note(...));
 *
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.3
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
}

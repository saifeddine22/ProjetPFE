package ma.pfe.bricovite.service;

import java.util.Optional;
import ma.pfe.bricovite.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Note}.
 */
public interface NoteService {
    /**
     * Save a note.
     *
     * @param note the entity to save.
     * @return the persisted entity.
     */
    Note save(Note note);

    /**
     * Updates a note.
     *
     * @param note the entity to update.
     * @return the persisted entity.
     */
    Note update(Note note);

    /**
     * Partially updates a note.
     *
     * @param note the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Note> partialUpdate(Note note);

    /**
     * Get all the notes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Note> findAll(Pageable pageable);

    /**
     * Get all the notes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Note> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" note.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Note> findOne(Long id);

    /**
     * Delete the "id" note.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

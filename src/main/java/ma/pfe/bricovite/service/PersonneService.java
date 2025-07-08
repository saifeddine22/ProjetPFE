package ma.pfe.bricovite.service;

import java.util.Optional;
import ma.pfe.bricovite.domain.Personne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Personne}.
 */
public interface PersonneService {
    /**
     * Save a personne.
     *
     * @param personne the entity to save.
     * @return the persisted entity.
     */
    Personne save(Personne personne);

    /**
     * Updates a personne.
     *
     * @param personne the entity to update.
     * @return the persisted entity.
     */
    Personne update(Personne personne);

    /**
     * Partially updates a personne.
     *
     * @param personne the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Personne> partialUpdate(Personne personne);

    /**
     * Get all the personnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Personne> findAll(Pageable pageable);

    /**
     * Get all the personnes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Personne> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" personne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Personne> findOne(Long id);

    /**
     * Delete the "id" personne.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<Personne> findByUserId(Long id);
}

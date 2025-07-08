package ma.pfe.bricovite.service;

import java.util.Optional;
import ma.pfe.bricovite.domain.Activite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Activite}.
 */
public interface ActiviteService {
    /**
     * Save a activite.
     *
     * @param activite the entity to save.
     * @return the persisted entity.
     */
    Activite save(Activite activite);

    /**
     * Updates a activite.
     *
     * @param activite the entity to update.
     * @return the persisted entity.
     */
    Activite update(Activite activite);

    /**
     * Partially updates a activite.
     *
     * @param activite the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Activite> partialUpdate(Activite activite);

    /**
     * Get all the activites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Activite> findAll(Pageable pageable);

    /**
     * Get all the activites with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Activite> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" activite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Activite> findOne(Long id);

    /**
     * Delete the "id" activite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

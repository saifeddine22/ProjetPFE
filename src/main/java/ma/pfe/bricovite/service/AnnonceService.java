package ma.pfe.bricovite.service;

import java.util.Optional;
import ma.pfe.bricovite.domain.Annonce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * Service Interface for managing {@link Annonce}.
 */
public interface AnnonceService {
    /**
     * Save a annonce.
     *
     * @param annonce the entity to save.
     * @return the persisted entity.
     */
    Annonce save(Annonce annonce);

    /**
     * Updates a annonce.
     *
     * @param annonce the entity to update.
     * @return the persisted entity.
     */
    Annonce update(Annonce annonce);

    /**
     * Partially updates a annonce.
     *
     * @param annonce the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Annonce> partialUpdate(Annonce annonce);

    /**
     * Get all the annonces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Annonce> findAll(Pageable pageable);

    /**
     * Get all the annonces with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Annonce> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" annonce.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Annonce> findOne(Long id);

    /**
     * Delete the "id" annonce.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<Annonce> findAllByUserId(Pageable pageable, Long id);

    Page<Annonce> search(String provinceId, String activiteId, String categorieId, Pageable pageable);
}

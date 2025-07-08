package ma.pfe.bricovite.service;

import java.util.Optional;
import ma.pfe.bricovite.domain.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Photo}.
 */
public interface PhotoService {
    /**
     * Save a photo.
     *
     * @param photo the entity to save.
     * @return the persisted entity.
     */
    Photo save(Photo photo);

    /**
     * Updates a photo.
     *
     * @param photo the entity to update.
     * @return the persisted entity.
     */
    Photo update(Photo photo);

    /**
     * Partially updates a photo.
     *
     * @param photo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Photo> partialUpdate(Photo photo);

    /**
     * Get all the photos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Photo> findAll(Pageable pageable);

    /**
     * Get the "id" photo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Photo> findOne(Long id);

    /**
     * @param idAnnonce
     * @return the entity
     */
    Page<Photo> findByAnnonceId(Pageable pageable, Long annonceId);

    /**
     * Delete the "id" photo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

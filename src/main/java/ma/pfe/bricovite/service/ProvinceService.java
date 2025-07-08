package ma.pfe.bricovite.service;

import java.util.Optional;
import ma.pfe.bricovite.domain.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Province}.
 */
public interface ProvinceService {
    /**
     * Save a province.
     *
     * @param province the entity to save.
     * @return the persisted entity.
     */
    Province save(Province province);

    /**
     * Updates a province.
     *
     * @param province the entity to update.
     * @return the persisted entity.
     */
    Province update(Province province);

    /**
     * Partially updates a province.
     *
     * @param province the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Province> partialUpdate(Province province);

    /**
     * Get all the provinces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Province> findAll(Pageable pageable);

    /**
     * Get all the provinces with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Province> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" province.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Province> findOne(Long id);

    /**
     * Delete the "id" province.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

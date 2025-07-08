package ma.pfe.bricovite.service.impl;

import java.util.Optional;
import ma.pfe.bricovite.domain.Activite;
import ma.pfe.bricovite.repository.ActiviteRepository;
import ma.pfe.bricovite.service.ActiviteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Activite}.
 */
@Service
@Transactional
public class ActiviteServiceImpl implements ActiviteService {

    private final Logger log = LoggerFactory.getLogger(ActiviteServiceImpl.class);

    private final ActiviteRepository activiteRepository;

    public ActiviteServiceImpl(ActiviteRepository activiteRepository) {
        this.activiteRepository = activiteRepository;
    }

    @Override
    public Activite save(Activite activite) {
        log.debug("Request to save Activite : {}", activite);
        return activiteRepository.save(activite);
    }

    @Override
    public Activite update(Activite activite) {
        log.debug("Request to save Activite : {}", activite);
        return activiteRepository.save(activite);
    }

    @Override
    public Optional<Activite> partialUpdate(Activite activite) {
        log.debug("Request to partially update Activite : {}", activite);

        return activiteRepository
            .findById(activite.getId())
            .map(existingActivite -> {
                if (activite.getNomFr() != null) {
                    existingActivite.setNomFr(activite.getNomFr());
                }
                if (activite.getNomAr() != null) {
                    existingActivite.setNomAr(activite.getNomAr());
                }
                if (activite.getCategorieFr() != null) {
                    existingActivite.setCategorieFr(activite.getCategorieFr());
                }
                if (activite.getCategorieAr() != null) {
                    existingActivite.setCategorieAr(activite.getCategorieAr());
                }

                return existingActivite;
            })
            .map(activiteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Activite> findAll(Pageable pageable) {
        log.debug("Request to get all Activites");
        return activiteRepository.findAll(pageable);
    }

    public Page<Activite> findAllWithEagerRelationships(Pageable pageable) {
        return activiteRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Activite> findOne(Long id) {
        log.debug("Request to get Activite : {}", id);
        return activiteRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Activite : {}", id);
        activiteRepository.deleteById(id);
    }
}

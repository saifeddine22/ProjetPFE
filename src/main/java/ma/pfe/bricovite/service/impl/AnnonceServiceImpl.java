package ma.pfe.bricovite.service.impl;

import java.util.Optional;
import ma.pfe.bricovite.domain.Annonce;
import ma.pfe.bricovite.repository.AnnonceRepository;
import ma.pfe.bricovite.service.AnnonceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Annonce}.
 */
@Service
@Transactional
public class AnnonceServiceImpl implements AnnonceService {

    private final Logger log = LoggerFactory.getLogger(AnnonceServiceImpl.class);

    private final AnnonceRepository annonceRepository;

    public AnnonceServiceImpl(AnnonceRepository annonceRepository) {
        this.annonceRepository = annonceRepository;
    }

    @Override
    public Annonce save(Annonce annonce) {
        log.debug("Request to save Annonce : {}", annonce);
        return annonceRepository.save(annonce);
    }

    @Override
    public Annonce update(Annonce annonce) {
        log.debug("Request to save Annonce : {}", annonce);
        return annonceRepository.save(annonce);
    }

    @Override
    public Optional<Annonce> partialUpdate(Annonce annonce) {
        log.debug("Request to partially update Annonce : {}", annonce);

        return annonceRepository
            .findById(annonce.getId())
            .map(existingAnnonce -> {
                if (annonce.getTitre() != null) {
                    existingAnnonce.setTitre(annonce.getTitre());
                }
                if (annonce.getDescription() != null) {
                    existingAnnonce.setDescription(annonce.getDescription());
                }
                if (annonce.getAdresse() != null) {
                    existingAnnonce.setAdresse(annonce.getAdresse());
                }
                if (annonce.getStatus() != null) {
                    existingAnnonce.setStatus(annonce.getStatus());
                }
                if (annonce.getDateAnnonce() != null) {
                    existingAnnonce.setDateAnnonce(annonce.getDateAnnonce());
                }
                if (annonce.getLatitude() != null) {
                    existingAnnonce.setLatitude(annonce.getLatitude());
                }
                if (annonce.getLongitude() != null) {
                    existingAnnonce.setLongitude(annonce.getLongitude());
                }

                return existingAnnonce;
            })
            .map(annonceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Annonce> findAll(Pageable pageable) {
        log.debug("Request to get all Annonces");
        return annonceRepository.findAll(pageable);
    }

    public Page<Annonce> findAllWithEagerRelationships(Pageable pageable) {
        return annonceRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Annonce> findOne(Long id) {
        log.debug("Request to get Annonce : {}", id);
        return annonceRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Annonce : {}", id);
        annonceRepository.deleteById(id);
    }

    @Override
    public Page<Annonce> findAllByUserId(Pageable pageable, Long id) {
        // TODO Auto-generated method stub
        return annonceRepository.findAllByUserId(pageable, id);
    }

    @Override
    public Page<Annonce> search(String provinceId, String activiteId, String categorieId, Pageable pageable) {
        // TODO Auto-generated method stub

        Integer p = Integer.parseInt(provinceId);
        Integer a = Integer.parseInt(activiteId);
        Integer c = Integer.parseInt(categorieId);
        return annonceRepository.search(p, a, c, pageable);
    }
}

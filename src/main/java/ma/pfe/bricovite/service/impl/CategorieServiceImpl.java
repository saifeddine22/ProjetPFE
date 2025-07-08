package ma.pfe.bricovite.service.impl;

import java.util.Optional;
import ma.pfe.bricovite.domain.Categorie;
import ma.pfe.bricovite.repository.CategorieRepository;
import ma.pfe.bricovite.service.CategorieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Categorie}.
 */
@Service
@Transactional
public class CategorieServiceImpl implements CategorieService {

    private final Logger log = LoggerFactory.getLogger(CategorieServiceImpl.class);

    private final CategorieRepository categorieRepository;

    public CategorieServiceImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    public Categorie save(Categorie categorie) {
        log.debug("Request to save Categorie : {}", categorie);
        return categorieRepository.save(categorie);
    }

    @Override
    public Categorie update(Categorie categorie) {
        log.debug("Request to save Categorie : {}", categorie);
        return categorieRepository.save(categorie);
    }

    @Override
    public Optional<Categorie> partialUpdate(Categorie categorie) {
        log.debug("Request to partially update Categorie : {}", categorie);

        return categorieRepository
            .findById(categorie.getId())
            .map(existingCategorie -> {
                if (categorie.getNomFr() != null) {
                    existingCategorie.setNomFr(categorie.getNomFr());
                }
                if (categorie.getNomAr() != null) {
                    existingCategorie.setNomAr(categorie.getNomAr());
                }

                return existingCategorie;
            })
            .map(categorieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categorie> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categorieRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categorie> findOne(Long id) {
        log.debug("Request to get Categorie : {}", id);
        return categorieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Categorie : {}", id);
        categorieRepository.deleteById(id);
    }
}

package ma.pfe.bricovite.service.impl;

import java.util.Optional;
import ma.pfe.bricovite.domain.Commune;
import ma.pfe.bricovite.repository.CommuneRepository;
import ma.pfe.bricovite.service.CommuneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Commune}.
 */
@Service
@Transactional
public class CommuneServiceImpl implements CommuneService {

    private final Logger log = LoggerFactory.getLogger(CommuneServiceImpl.class);

    private final CommuneRepository communeRepository;

    public CommuneServiceImpl(CommuneRepository communeRepository) {
        this.communeRepository = communeRepository;
    }

    @Override
    public Commune save(Commune commune) {
        log.debug("Request to save Commune : {}", commune);
        return communeRepository.save(commune);
    }

    @Override
    public Commune update(Commune commune) {
        log.debug("Request to save Commune : {}", commune);
        return communeRepository.save(commune);
    }

    @Override
    public Optional<Commune> partialUpdate(Commune commune) {
        log.debug("Request to partially update Commune : {}", commune);

        return communeRepository
            .findById(commune.getId())
            .map(existingCommune -> {
                if (commune.getCodeReg() != null) {
                    existingCommune.setCodeReg(commune.getCodeReg());
                }
                if (commune.getCodeProv() != null) {
                    existingCommune.setCodeProv(commune.getCodeProv());
                }
                if (commune.getProvinceFr() != null) {
                    existingCommune.setProvinceFr(commune.getProvinceFr());
                }
                if (commune.getProvinceAr() != null) {
                    existingCommune.setProvinceAr(commune.getProvinceAr());
                }
                if (commune.getRegionFr() != null) {
                    existingCommune.setRegionFr(commune.getRegionFr());
                }
                if (commune.getRegionAr() != null) {
                    existingCommune.setRegionAr(commune.getRegionAr());
                }
                if (commune.getCercleFr() != null) {
                    existingCommune.setCercleFr(commune.getCercleFr());
                }
                if (commune.getCodeCercle() != null) {
                    existingCommune.setCodeCercle(commune.getCodeCercle());
                }
                if (commune.getComFr() != null) {
                    existingCommune.setComFr(commune.getComFr());
                }
                if (commune.getCodeCom() != null) {
                    existingCommune.setCodeCom(commune.getCodeCom());
                }
                if (commune.getCentreFr() != null) {
                    existingCommune.setCentreFr(commune.getCentreFr());
                }
                if (commune.getCodAc() != null) {
                    existingCommune.setCodAc(commune.getCodAc());
                }
                if (commune.getComAr() != null) {
                    existingCommune.setComAr(commune.getComAr());
                }
                if (commune.getCc() != null) {
                    existingCommune.setCc(commune.getCc());
                }
                if (commune.getCentreAr() != null) {
                    existingCommune.setCentreAr(commune.getCentreAr());
                }
                if (commune.getNomAr() != null) {
                    existingCommune.setNomAr(commune.getNomAr());
                }
                if (commune.getNomFr() != null) {
                    existingCommune.setNomFr(commune.getNomFr());
                }
                if (commune.getGeometry() != null) {
                    existingCommune.setGeometry(commune.getGeometry());
                }

                return existingCommune;
            })
            .map(communeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Commune> findAll(Pageable pageable) {
        log.debug("Request to get all Communes");
        return communeRepository.findAll(pageable);
    }

    public Page<Commune> findAllWithEagerRelationships(Pageable pageable) {
        return communeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Commune> findOne(Long id) {
        log.debug("Request to get Commune : {}", id);
        return communeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commune : {}", id);
        communeRepository.deleteById(id);
    }
}

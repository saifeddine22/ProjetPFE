package ma.pfe.bricovite.service.impl;

import java.util.Optional;
import ma.pfe.bricovite.domain.Province;
import ma.pfe.bricovite.repository.ProvinceRepository;
import ma.pfe.bricovite.service.ProvinceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Province}.
 */
@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService {

    private final Logger log = LoggerFactory.getLogger(ProvinceServiceImpl.class);

    private final ProvinceRepository provinceRepository;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    @Override
    public Province save(Province province) {
        log.debug("Request to save Province : {}", province);
        return provinceRepository.save(province);
    }

    @Override
    public Province update(Province province) {
        log.debug("Request to save Province : {}", province);
        return provinceRepository.save(province);
    }

    @Override
    public Optional<Province> partialUpdate(Province province) {
        log.debug("Request to partially update Province : {}", province);

        return provinceRepository
            .findById(province.getId())
            .map(existingProvince -> {
                if (province.getCodeReg() != null) {
                    existingProvince.setCodeReg(province.getCodeReg());
                }
                if (province.getCodeProv() != null) {
                    existingProvince.setCodeProv(province.getCodeProv());
                }
                if (province.getNomFr() != null) {
                    existingProvince.setNomFr(province.getNomFr());
                }
                if (province.getNomAr() != null) {
                    existingProvince.setNomAr(province.getNomAr());
                }
                if (province.getRegionFr() != null) {
                    existingProvince.setRegionFr(province.getRegionFr());
                }
                if (province.getRegionAr() != null) {
                    existingProvince.setRegionAr(province.getRegionAr());
                }
                if (province.getGeometry() != null) {
                    existingProvince.setGeometry(province.getGeometry());
                }

                return existingProvince;
            })
            .map(provinceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Province> findAll(Pageable pageable) {
        log.debug("Request to get all Provinces");
        return provinceRepository.findAll(pageable);
    }

    public Page<Province> findAllWithEagerRelationships(Pageable pageable) {
        return provinceRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Province> findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        return provinceRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.deleteById(id);
    }
}

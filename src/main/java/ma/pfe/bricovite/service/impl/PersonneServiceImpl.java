package ma.pfe.bricovite.service.impl;

import java.util.Optional;
import ma.pfe.bricovite.domain.Personne;
import ma.pfe.bricovite.repository.PersonneRepository;
import ma.pfe.bricovite.service.PersonneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Personne}.
 */
@Service
@Transactional
public class PersonneServiceImpl implements PersonneService {

    private final Logger log = LoggerFactory.getLogger(PersonneServiceImpl.class);

    private final PersonneRepository personneRepository;

    public PersonneServiceImpl(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    @Override
    public Personne save(Personne personne) {
        log.debug("Request to save Personne : {}", personne);
        return personneRepository.save(personne);
    }

    @Override
    public Personne update(Personne personne) {
        log.debug("Request to save Personne : {}", personne);
        return personneRepository.save(personne);
    }

    @Override
    public Optional<Personne> partialUpdate(Personne personne) {
        log.debug("Request to partially update Personne : {}", personne);

        return personneRepository
            .findById(personne.getId())
            .map(existingPersonne -> {
                if (personne.getCnie() != null) {
                    existingPersonne.setCnie(personne.getCnie());
                }

                if (personne.getTel() != null) {
                    existingPersonne.setTel(personne.getTel());
                }

                return existingPersonne;
            })
            .map(personneRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Personne> findAll(Pageable pageable) {
        log.debug("Request to get all Personnes");
        return personneRepository.findAll(pageable);
    }

    public Page<Personne> findAllWithEagerRelationships(Pageable pageable) {
        return personneRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Personne> findOne(Long id) {
        log.debug("Request to get Personne : {}", id);
        return personneRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Personne : {}", id);
        personneRepository.deleteById(id);
    }

    @Override
    public Optional<Personne> findByUserId(Long id) {
        // TODO Auto-generated method stub
        return personneRepository.findByUserId(id);
    }
}

package ma.pfe.bricovite.service.impl;

import java.util.Optional;
import ma.pfe.bricovite.domain.Commentaire;
import ma.pfe.bricovite.repository.CommentaireRepository;
import ma.pfe.bricovite.service.CommentaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Commentaire}.
 */
@Service
@Transactional
public class CommentaireServiceImpl implements CommentaireService {

    private final Logger log = LoggerFactory.getLogger(CommentaireServiceImpl.class);

    private final CommentaireRepository commentaireRepository;

    public CommentaireServiceImpl(CommentaireRepository commentaireRepository) {
        this.commentaireRepository = commentaireRepository;
    }

    @Override
    public Commentaire save(Commentaire commentaire) {
        log.debug("Request to save Commentaire : {}", commentaire);
        return commentaireRepository.save(commentaire);
    }

    @Override
    public Commentaire update(Commentaire commentaire) {
        log.debug("Request to save Commentaire : {}", commentaire);
        return commentaireRepository.save(commentaire);
    }

    @Override
    public Optional<Commentaire> partialUpdate(Commentaire commentaire) {
        log.debug("Request to partially update Commentaire : {}", commentaire);

        return commentaireRepository
            .findById(commentaire.getId())
            .map(existingCommentaire -> {
                if (commentaire.getDetails() != null) {
                    existingCommentaire.setDetails(commentaire.getDetails());
                }
                if (commentaire.getDateCommentaire() != null) {
                    existingCommentaire.setDateCommentaire(commentaire.getDateCommentaire());
                }

                return existingCommentaire;
            })
            .map(commentaireRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Commentaire> findAll(Pageable pageable) {
        log.debug("Request to get all Commentaires");
        return commentaireRepository.findAll(pageable);
    }

    public Page<Commentaire> findAllWithEagerRelationships(Pageable pageable) {
        return commentaireRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Commentaire> findOne(Long id) {
        log.debug("Request to get Commentaire : {}", id);
        return commentaireRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commentaire : {}", id);
        commentaireRepository.deleteById(id);
    }

    @Override
    public Page<Commentaire> findByAnnonceId(Pageable pageable, Long annonceId) {
        // TODO Auto-generated method stub
        return commentaireRepository.findByAnnonceId(pageable, annonceId);
    }
}

package ma.pfe.bricovite.service.impl;

import java.util.Optional;
import ma.pfe.bricovite.domain.Photo;
import ma.pfe.bricovite.repository.PhotoRepository;
import ma.pfe.bricovite.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Photo}.
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public Photo save(Photo photo) {
        log.debug("Request to save Photo : {}", photo);
        return photoRepository.save(photo);
    }

    @Override
    public Photo update(Photo photo) {
        log.debug("Request to save Photo : {}", photo);
        return photoRepository.save(photo);
    }

    @Override
    public Optional<Photo> partialUpdate(Photo photo) {
        log.debug("Request to partially update Photo : {}", photo);

        return photoRepository
            .findById(photo.getId())
            .map(existingPhoto -> {
                if (photo.getUrl() != null) {
                    existingPhoto.setUrl(photo.getUrl());
                }
                if (photo.getLibelle() != null) {
                    existingPhoto.setLibelle(photo.getLibelle());
                }
                if (photo.getImage() != null) {
                    existingPhoto.setImage(photo.getImage());
                }
                if (photo.getImageContentType() != null) {
                    existingPhoto.setImageContentType(photo.getImageContentType());
                }

                return existingPhoto;
            })
            .map(photoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Photo> findAll(Pageable pageable) {
        log.debug("Request to get all Photos");
        return photoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Photo> findOne(Long id) {
        log.debug("Request to get Photo : {}", id);
        return photoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Photo> findByAnnonceId(Pageable pageable, Long annonceId) {
        //log.debug("Request to get Photo : {}", idAnnonce);
        return photoRepository.findByAnnonceId(pageable, annonceId);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Photo : {}", id);
        photoRepository.deleteById(id);
    }
}

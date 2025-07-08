package ma.pfe.bricovite.repository;

import java.util.List;
import ma.pfe.bricovite.domain.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Photo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Page<Photo> findByAnnonceId(Pageable pageable, Long annonceId);
}

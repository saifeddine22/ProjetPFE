package ma.pfe.bricovite.repository;

import java.util.List;
import java.util.Optional;
import ma.pfe.bricovite.domain.Commune;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Commune entity.
 */
@Repository
public interface CommuneRepository extends JpaRepository<Commune, Long> {
    default Optional<Commune> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Commune> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Commune> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct commune from Commune commune left join fetch commune.province",
        countQuery = "select count(distinct commune) from Commune commune"
    )
    Page<Commune> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct commune from Commune commune left join fetch commune.province")
    List<Commune> findAllWithToOneRelationships();

    @Query("select commune from Commune commune left join fetch commune.province where commune.id =:id")
    Optional<Commune> findOneWithToOneRelationships(@Param("id") Long id);
}

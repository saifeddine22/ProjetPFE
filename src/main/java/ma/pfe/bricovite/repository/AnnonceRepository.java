package ma.pfe.bricovite.repository;

import java.util.List;
import java.util.Optional;
import ma.pfe.bricovite.domain.Annonce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Annonce entity.
 */
@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long> {
    @Query("select annonce from Annonce annonce where annonce.user.login = ?#{principal.username}")
    List<Annonce> findByUserIsCurrentUser();

    default Optional<Annonce> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Annonce> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Annonce> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct annonce from Annonce annonce left join fetch annonce.user left join fetch annonce.commune left join fetch annonce.activite",
        countQuery = "select count(distinct annonce) from Annonce annonce"
    )
    Page<Annonce> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct annonce from Annonce annonce left join fetch annonce.user left join fetch annonce.commune left join fetch annonce.activite"
    )
    List<Annonce> findAllWithToOneRelationships();

    @Query(
        "select annonce from Annonce annonce left join fetch annonce.user left join fetch annonce.commune left join fetch annonce.activite where annonce.id =:id"
    )
    Optional<Annonce> findOneWithToOneRelationships(@Param("id") Long id);

    Page<Annonce> findAllByUserId(Pageable pageable, Long id);

    Page<Annonce> findByActiviteId(Pageable pageable, Long id);

    @Query(
        "select a from Annonce a where  " +
        "(?1 = -1 or a.commune.province.id = ?1) " +
        "and (?2 = -1 or a.activite.id = ?2) " +
        "and (?3 = -1 or a.activite.categorie.id = ?3 )"
    )
    Page<Annonce> search(Integer provinceId, Integer activiteId, Integer categorieId, Pageable pageable);
}

package ma.pfe.bricovite.repository;

import java.util.List;
import java.util.Optional;
import ma.pfe.bricovite.domain.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Province entity.
 */
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    default Optional<Province> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Province> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Province> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct province from Province province left join fetch province.region",
        countQuery = "select count(distinct province) from Province province"
    )
    Page<Province> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct province from Province province left join fetch province.region")
    List<Province> findAllWithToOneRelationships();

    @Query("select province from Province province left join fetch province.region where province.id =:id")
    Optional<Province> findOneWithToOneRelationships(@Param("id") Long id);
}

package com.promition.drugwiki.repository;

import com.promition.drugwiki.domain.Brand;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Brand entity.
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
    @Query(
        value = "select distinct brand from Brand brand left join fetch brand.genericsuseds",
        countQuery = "select count(distinct brand) from Brand brand"
    )
    Page<Brand> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct brand from Brand brand left join fetch brand.genericsuseds")
    List<Brand> findAllWithEagerRelationships();

    @Query("select brand from Brand brand left join fetch brand.genericsuseds where brand.id =:id")
    Optional<Brand> findOneWithEagerRelationships(@Param("id") Long id);

    List<Brand> findAllByBnameContains(String name);

    Page<Brand> findAllByBnameContains(String name, Pageable pageable);
}

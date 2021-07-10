package com.promition.drugwiki.repository;

import com.promition.drugwiki.domain.Generics;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data SQL repository for the Generics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenericsRepository extends JpaRepository<Generics, Long>, JpaSpecificationExecutor<Generics> {
    List<Generics> findAllByGnameContains(String name);
    Page<Generics> findAllByGnameContains(String name, Pageable pageable);
}

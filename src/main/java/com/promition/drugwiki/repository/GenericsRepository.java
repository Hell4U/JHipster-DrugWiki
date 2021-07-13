package com.promition.drugwiki.repository;

import com.promition.drugwiki.domain.Generics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Generics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenericsRepository extends JpaRepository<Generics, Long>, JpaSpecificationExecutor<Generics> {}

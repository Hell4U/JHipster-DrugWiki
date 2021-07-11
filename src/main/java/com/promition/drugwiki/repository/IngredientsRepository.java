package com.promition.drugwiki.repository;

import com.promition.drugwiki.domain.Ingredients;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ingredients entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngredientsRepository extends JpaRepository<Ingredients, Long>, JpaSpecificationExecutor<Ingredients> {
    List<Ingredients> findAllByInameContains(String name);
}

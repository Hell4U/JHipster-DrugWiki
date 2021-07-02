package com.promition.drugwiki.repository;

import com.promition.drugwiki.domain.Company;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    List<Company> findAllByCnameContains(String name);
}

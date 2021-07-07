package com.promition.drugwiki.repository.search;

import com.promition.drugwiki.domain.Company;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Company} entity.
 */
public interface CompanySearchRepository extends ElasticsearchRepository<Company, Long> {}

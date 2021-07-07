package com.promition.drugwiki.repository.search;

import com.promition.drugwiki.domain.Brand;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Brand} entity.
 */
public interface BrandSearchRepository extends ElasticsearchRepository<Brand, Long> {}

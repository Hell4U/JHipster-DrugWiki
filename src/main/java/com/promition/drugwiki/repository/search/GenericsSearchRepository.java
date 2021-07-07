package com.promition.drugwiki.repository.search;

import com.promition.drugwiki.domain.Generics;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Generics} entity.
 */
public interface GenericsSearchRepository extends ElasticsearchRepository<Generics, Long> {}

package com.promition.drugwiki.repository.search;

import com.promition.drugwiki.domain.Ingredients;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Ingredients} entity.
 */
public interface IngredientsSearchRepository extends ElasticsearchRepository<Ingredients, Long> {}

package com.promition.drugwiki.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link GenericsSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class GenericsSearchRepositoryMockConfiguration {

    @MockBean
    private GenericsSearchRepository mockGenericsSearchRepository;
}

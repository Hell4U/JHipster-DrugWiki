package com.mycompany.myapp.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.repository.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
/**
 * Test class for the SearchResource REST controller.
 *
 * @see SearchResource
 */
@IntegrationTest
class SearchResourceIT {

    private MockMvc restMockMvc;

    private com.mycompany.myapp.repository.BrandRepository brandRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        SearchResource searchResource = new SearchResource(brandRepository);
        restMockMvc = MockMvcBuilders.standaloneSetup(searchResource).build();
    }

    /**
     * Test search
     */
    @Test
    void testSearch() throws Exception {
        restMockMvc.perform(get("/api/search/search")).andExpect(status().isOk());
    }
}

package com.promition.drugwiki.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.promition.drugwiki.IntegrationTest;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        SearchResource searchResource = new SearchResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(searchResource).build();
    }

    /**
     * Test searchBrand
     */
    @Test
    void testSearchBrand() throws Exception {
        restMockMvc.perform(get("/api/search/search-brand")).andExpect(status().isOk());
    }

    /**
     * Test searchCompany
     */
    @Test
    void testSearchCompany() throws Exception {
        restMockMvc.perform(get("/api/search/search-company")).andExpect(status().isOk());
    }

    /**
     * Test searchIngredient
     */
    @Test
    void testSearchIngredient() throws Exception {
        restMockMvc.perform(get("/api/search/search-ingredient")).andExpect(status().isOk());
    }
}

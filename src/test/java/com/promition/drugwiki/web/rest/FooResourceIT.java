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
 * Test class for the FooResource REST controller.
 *
 * @see FooResource
 */
@IntegrationTest
class FooResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        FooResource fooResource = new FooResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(fooResource).build();
    }

    /**
     * Test foos
     */
    @Test
    void testFoos() throws Exception {
        restMockMvc.perform(get("/api/foo/foos")).andExpect(status().isOk());
    }

    /**
     * Test boos
     */
    @Test
    void testBoos() throws Exception {
        restMockMvc.perform(get("/api/foo/boos")).andExpect(status().isOk());
    }
}

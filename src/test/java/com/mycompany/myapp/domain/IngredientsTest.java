package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngredientsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ingredients.class);
        Ingredients ingredients1 = new Ingredients();
        ingredients1.setId(1L);
        Ingredients ingredients2 = new Ingredients();
        ingredients2.setId(ingredients1.getId());
        assertThat(ingredients1).isEqualTo(ingredients2);
        ingredients2.setId(2L);
        assertThat(ingredients1).isNotEqualTo(ingredients2);
        ingredients1.setId(null);
        assertThat(ingredients1).isNotEqualTo(ingredients2);
    }
}

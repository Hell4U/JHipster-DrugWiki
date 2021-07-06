package com.promition.drugwiki.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.promition.drugwiki.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngredientsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngredientsDTO.class);
        IngredientsDTO ingredientsDTO1 = new IngredientsDTO();
        ingredientsDTO1.setId(1L);
        IngredientsDTO ingredientsDTO2 = new IngredientsDTO();
        assertThat(ingredientsDTO1).isNotEqualTo(ingredientsDTO2);
        ingredientsDTO2.setId(ingredientsDTO1.getId());
        assertThat(ingredientsDTO1).isEqualTo(ingredientsDTO2);
        ingredientsDTO2.setId(2L);
        assertThat(ingredientsDTO1).isNotEqualTo(ingredientsDTO2);
        ingredientsDTO1.setId(null);
        assertThat(ingredientsDTO1).isNotEqualTo(ingredientsDTO2);
    }
}

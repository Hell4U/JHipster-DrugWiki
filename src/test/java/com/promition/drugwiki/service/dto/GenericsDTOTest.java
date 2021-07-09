package com.promition.drugwiki.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.promition.drugwiki.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenericsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenericsDTO.class);
        GenericsDTO genericsDTO1 = new GenericsDTO();
        genericsDTO1.setId(1L);
        GenericsDTO genericsDTO2 = new GenericsDTO();
        assertThat(genericsDTO1).isNotEqualTo(genericsDTO2);
        genericsDTO2.setId(genericsDTO1.getId());
        assertThat(genericsDTO1).isEqualTo(genericsDTO2);
        genericsDTO2.setId(2L);
        assertThat(genericsDTO1).isNotEqualTo(genericsDTO2);
        genericsDTO1.setId(null);
        assertThat(genericsDTO1).isNotEqualTo(genericsDTO2);
    }
}

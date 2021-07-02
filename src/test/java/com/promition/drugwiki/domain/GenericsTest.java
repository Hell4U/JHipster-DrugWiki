package com.promition.drugwiki.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.promition.drugwiki.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenericsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Generics.class);
        Generics generics1 = new Generics();
        generics1.setId(1L);
        Generics generics2 = new Generics();
        generics2.setId(generics1.getId());
        assertThat(generics1).isEqualTo(generics2);
        generics2.setId(2L);
        assertThat(generics1).isNotEqualTo(generics2);
        generics1.setId(null);
        assertThat(generics1).isNotEqualTo(generics2);
    }
}

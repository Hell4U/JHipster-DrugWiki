package com.promition.drugwiki.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenericsMapperTest {

    private GenericsMapper genericsMapper;

    @BeforeEach
    public void setUp() {
        genericsMapper = new GenericsMapperImpl();
    }
}

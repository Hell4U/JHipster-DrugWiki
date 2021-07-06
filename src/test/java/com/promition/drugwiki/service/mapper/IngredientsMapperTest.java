package com.promition.drugwiki.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IngredientsMapperTest {

    private IngredientsMapper ingredientsMapper;

    @BeforeEach
    public void setUp() {
        ingredientsMapper = new IngredientsMapperImpl();
    }
}

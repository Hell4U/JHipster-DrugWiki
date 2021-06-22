package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Ingredients;
import com.mycompany.myapp.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Ingredients}, with proper type conversions.
 */
@Service
public class IngredientsRowMapper implements BiFunction<Row, String, Ingredients> {

    private final ColumnConverter converter;

    public IngredientsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Ingredients} stored in the database.
     */
    @Override
    public Ingredients apply(Row row, String prefix) {
        Ingredients entity = new Ingredients();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIname(converter.fromRow(row, prefix + "_iname", String.class));
        entity.setSymptoms(converter.fromRow(row, prefix + "_symptoms", String.class));
        entity.setSideeffects(converter.fromRow(row, prefix + "_sideeffects", String.class));
        entity.setCautions(converter.fromRow(row, prefix + "_cautions", String.class));
        return entity;
    }
}

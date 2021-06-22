package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Generics;
import com.mycompany.myapp.domain.enumeration.DosageUnit;
import com.mycompany.myapp.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Generics}, with proper type conversions.
 */
@Service
public class GenericsRowMapper implements BiFunction<Row, String, Generics> {

    private final ColumnConverter converter;

    public GenericsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Generics} stored in the database.
     */
    @Override
    public Generics apply(Row row, String prefix) {
        Generics entity = new Generics();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDosage(converter.fromRow(row, prefix + "_dosage", Double.class));
        entity.setDosageunit(converter.fromRow(row, prefix + "_dosageunit", DosageUnit.class));
        entity.setIngredientNameId(converter.fromRow(row, prefix + "_ingredient_name_id", Long.class));
        return entity;
    }
}

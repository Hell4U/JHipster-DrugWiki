package com.promition.drugwiki.repository.rowmapper;

import com.promition.drugwiki.domain.Brand;
import com.promition.drugwiki.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Brand}, with proper type conversions.
 */
@Service
public class BrandRowMapper implements BiFunction<Row, String, Brand> {

    private final ColumnConverter converter;

    public BrandRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Brand} stored in the database.
     */
    @Override
    public Brand apply(Row row, String prefix) {
        Brand entity = new Brand();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", Float.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCompanyId(converter.fromRow(row, prefix + "_company_id", Long.class));
        return entity;
    }
}

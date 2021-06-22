package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Company}, with proper type conversions.
 */
@Service
public class CompanyRowMapper implements BiFunction<Row, String, Company> {

    private final ColumnConverter converter;

    public CompanyRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Company} stored in the database.
     */
    @Override
    public Company apply(Row row, String prefix) {
        Company entity = new Company();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCname(converter.fromRow(row, prefix + "_cname", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setWebsite(converter.fromRow(row, prefix + "_website", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setFax(converter.fromRow(row, prefix + "_fax", String.class));
        entity.setPhoneno(converter.fromRow(row, prefix + "_phoneno", String.class));
        return entity;
    }
}

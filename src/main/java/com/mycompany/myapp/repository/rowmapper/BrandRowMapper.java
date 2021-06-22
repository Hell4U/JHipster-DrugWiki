package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Brand;
import com.mycompany.myapp.domain.enumeration.BrandType;
import com.mycompany.myapp.domain.enumeration.TypeUnit;
import com.mycompany.myapp.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
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
        entity.setBname(converter.fromRow(row, prefix + "_bname", String.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", Double.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", LocalDate.class));
        entity.setPackageunit(converter.fromRow(row, prefix + "_packageunit", Double.class));
        entity.setType(converter.fromRow(row, prefix + "_type", BrandType.class));
        entity.setTypeunit(converter.fromRow(row, prefix + "_typeunit", TypeUnit.class));
        entity.setCompanyofMedicineId(converter.fromRow(row, prefix + "_companyof_medicine_id", Long.class));
        return entity;
    }
}

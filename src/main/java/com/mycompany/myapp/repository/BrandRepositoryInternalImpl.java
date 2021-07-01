package com.mycompany.myapp.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.mycompany.myapp.domain.Brand;
import com.mycompany.myapp.domain.Generics;
import com.mycompany.myapp.repository.rowmapper.BrandRowMapper;
import com.mycompany.myapp.repository.rowmapper.CompanyRowMapper;
import com.mycompany.myapp.service.EntityManager;
import com.mycompany.myapp.service.EntityManager.LinkTable;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Brand entity.
 */
@SuppressWarnings("unused")
class BrandRepositoryInternalImpl implements BrandRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CompanyRowMapper companyMapper;
    private final BrandRowMapper brandMapper;

    private static final Table entityTable = Table.aliased("brand", EntityManager.ENTITY_ALIAS);
    private static final Table companyofMedicineTable = Table.aliased("company", "companyofMedicine");

    private static final EntityManager.LinkTable genericsLink = new LinkTable("rel_brand__generics", "brand_id", "generics_id");

    public BrandRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CompanyRowMapper companyMapper,
        BrandRowMapper brandMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.companyMapper = companyMapper;
        this.brandMapper = brandMapper;
    }

    @Override
    public Flux<Brand> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }



    @Override
    public Flux<Brand> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    @Override
    public Flux<Brand> findAllByBname(String name) {
        return findAllByBname(name);
    }



    RowsFetchSpec<Brand> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = BrandSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CompanySqlHelper.getColumns(companyofMedicineTable, "companyofMedicine"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(companyofMedicineTable)
            .on(Column.create("companyof_medicine_id", entityTable))
            .equals(Column.create("id", companyofMedicineTable));

        String select = entityManager.createSelect(selectFrom, Brand.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(
                crit ->
                    new StringBuilder(select)
                        .append(" ")
                        .append("WHERE")
                        .append(" ")
                        .append(alias)
                        .append(".")
                        .append(crit.toString())
                        .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<Brand> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Brand> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    @Override
    public Mono<Brand> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Brand> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Brand> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Brand process(Row row, RowMetadata metadata) {
        Brand entity = brandMapper.apply(row, "e");
        entity.setCompanyofMedicine(companyMapper.apply(row, "companyofMedicine"));
        return entity;
    }

    @Override
    public <S extends Brand> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Brand> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity).flatMap(savedEntity -> updateRelations(savedEntity));
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Brand with id = " + entity.getId());
                        }
                        return entity;
                    }
                )
                .then(updateRelations(entity));
        }
    }

    @Override
    public Mono<Integer> update(Brand entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId)
            .then(r2dbcEntityTemplate.delete(Brand.class).matching(query(where("id").is(entityId))).all().then());
    }

    protected <S extends Brand> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(genericsLink, entity.getId(), entity.getGenerics().stream().map(Generics::getId))
            .then();
        return result.thenReturn(entity);
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(genericsLink, entityId);
    }
}

class BrandSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("bname", table, columnPrefix + "_bname"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("packageunit", table, columnPrefix + "_packageunit"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("typeunit", table, columnPrefix + "_typeunit"));

        columns.add(Column.aliased("companyof_medicine_id", table, columnPrefix + "_companyof_medicine_id"));
        return columns;
    }
}

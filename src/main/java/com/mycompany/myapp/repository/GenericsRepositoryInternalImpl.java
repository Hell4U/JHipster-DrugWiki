package com.mycompany.myapp.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.mycompany.myapp.domain.Generics;
import com.mycompany.myapp.domain.enumeration.DosageUnit;
import com.mycompany.myapp.repository.rowmapper.GenericsRowMapper;
import com.mycompany.myapp.repository.rowmapper.IngredientsRowMapper;
import com.mycompany.myapp.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
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
 * Spring Data SQL reactive custom repository implementation for the Generics entity.
 */
@SuppressWarnings("unused")
class GenericsRepositoryInternalImpl implements GenericsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final IngredientsRowMapper ingredientsMapper;
    private final GenericsRowMapper genericsMapper;

    private static final Table entityTable = Table.aliased("generics", EntityManager.ENTITY_ALIAS);
    private static final Table ingredientNameTable = Table.aliased("ingredients", "ingredientName");

    public GenericsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        IngredientsRowMapper ingredientsMapper,
        GenericsRowMapper genericsMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.ingredientsMapper = ingredientsMapper;
        this.genericsMapper = genericsMapper;
    }

    @Override
    public Flux<Generics> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Generics> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Generics> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = GenericsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(IngredientsSqlHelper.getColumns(ingredientNameTable, "ingredientName"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(ingredientNameTable)
            .on(Column.create("ingredient_name_id", entityTable))
            .equals(Column.create("id", ingredientNameTable));

        String select = entityManager.createSelect(selectFrom, Generics.class, pageable, criteria);
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
    public Flux<Generics> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Generics> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Generics process(Row row, RowMetadata metadata) {
        Generics entity = genericsMapper.apply(row, "e");
        entity.setIngredientName(ingredientsMapper.apply(row, "ingredientName"));
        return entity;
    }

    @Override
    public <S extends Generics> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Generics> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Generics with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Generics entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class GenericsSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("dosage", table, columnPrefix + "_dosage"));
        columns.add(Column.aliased("dosageunit", table, columnPrefix + "_dosageunit"));

        columns.add(Column.aliased("ingredient_name_id", table, columnPrefix + "_ingredient_name_id"));
        return columns;
    }
}

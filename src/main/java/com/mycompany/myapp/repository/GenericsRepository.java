package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Generics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Generics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenericsRepository extends R2dbcRepository<Generics, Long>, GenericsRepositoryInternal {
    Flux<Generics> findAllBy(Pageable pageable);

    @Query("SELECT * FROM generics entity WHERE entity.ingredient_name_id = :id")
    Flux<Generics> findByIngredientName(Long id);

    @Query("SELECT * FROM generics entity WHERE entity.ingredient_name_id IS NULL")
    Flux<Generics> findAllWhereIngredientNameIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Generics> findAll();

    @Override
    Mono<Generics> findById(Long id);

    @Override
    <S extends Generics> Mono<S> save(S entity);
}

interface GenericsRepositoryInternal {
    <S extends Generics> Mono<S> insert(S entity);
    <S extends Generics> Mono<S> save(S entity);
    Mono<Integer> update(Generics entity);

    Flux<Generics> findAll();
    Mono<Generics> findById(Long id);
    Flux<Generics> findAllBy(Pageable pageable);
    Flux<Generics> findAllBy(Pageable pageable, Criteria criteria);
}

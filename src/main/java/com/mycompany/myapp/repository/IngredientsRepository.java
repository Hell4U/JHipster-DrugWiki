package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ingredients;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Ingredients entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngredientsRepository extends R2dbcRepository<Ingredients, Long>, IngredientsRepositoryInternal {
    Flux<Ingredients> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Ingredients> findAll();

    @Override
    Mono<Ingredients> findById(Long id);

    @Override
    <S extends Ingredients> Mono<S> save(S entity);
}

interface IngredientsRepositoryInternal {
    <S extends Ingredients> Mono<S> insert(S entity);
    <S extends Ingredients> Mono<S> save(S entity);
    Mono<Integer> update(Ingredients entity);

    Flux<Ingredients> findAll();
    Mono<Ingredients> findById(Long id);
    Flux<Ingredients> findAllBy(Pageable pageable);
    Flux<Ingredients> findAllBy(Pageable pageable, Criteria criteria);
}

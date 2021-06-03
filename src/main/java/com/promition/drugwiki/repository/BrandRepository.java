package com.promition.drugwiki.repository;

import com.promition.drugwiki.domain.Brand;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Brand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrandRepository extends R2dbcRepository<Brand, Long>, BrandRepositoryInternal {
    @Query("SELECT * FROM brand entity WHERE entity.company_id = :id")
    Flux<Brand> findByCompany(Long id);

    @Query("SELECT * FROM brand entity WHERE entity.company_id IS NULL")
    Flux<Brand> findAllWhereCompanyIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Brand> findAll();

    @Override
    Mono<Brand> findById(Long id);

    @Override
    <S extends Brand> Mono<S> save(S entity);
}

interface BrandRepositoryInternal {
    <S extends Brand> Mono<S> insert(S entity);
    <S extends Brand> Mono<S> save(S entity);
    Mono<Integer> update(Brand entity);

    Flux<Brand> findAll();
    Mono<Brand> findById(Long id);
    Flux<Brand> findAllBy(Pageable pageable);
    Flux<Brand> findAllBy(Pageable pageable, Criteria criteria);
}

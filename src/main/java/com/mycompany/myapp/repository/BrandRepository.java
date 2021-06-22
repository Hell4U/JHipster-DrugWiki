package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Brand;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Spring Data SQL reactive repository for the Brand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrandRepository extends R2dbcRepository<Brand, Long>, BrandRepositoryInternal {
    Flux<Brand> findAllBy(Pageable pageable);

    @Override
    Mono<Brand> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Brand> findAllWithEagerRelationships();

    @Override
    Flux<Brand> findAllWithEagerRelationships(Pageable page);

    List<Brand> findAllByBnameContaining(String name);

    @Override
    Mono<Void> deleteById(Long id);

    @Query("SELECT * FROM brand entity WHERE entity.companyof_medicine_id = :id")
    Flux<Brand> findByCompanyofMedicine(Long id);

    @Query("SELECT * FROM brand entity WHERE entity.companyof_medicine_id IS NULL")
    Flux<Brand> findAllWhereCompanyofMedicineIsNull();

    @Query(
        "SELECT entity.* FROM brand entity JOIN rel_brand__generics joinTable ON entity.id = joinTable.brand_id WHERE joinTable.generics_id = :id"
    )
    Flux<Brand> findByGenerics(Long id);

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
    List<Brand> findAllByBnameContaining(String name);

    Mono<Brand> findOneWithEagerRelationships(Long id);

    Flux<Brand> findAllWithEagerRelationships();

    Flux<Brand> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

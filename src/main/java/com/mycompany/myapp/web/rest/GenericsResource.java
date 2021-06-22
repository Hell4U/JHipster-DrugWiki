package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Generics;
import com.mycompany.myapp.repository.GenericsRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Generics}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GenericsResource {

    private final Logger log = LoggerFactory.getLogger(GenericsResource.class);

    private static final String ENTITY_NAME = "generics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenericsRepository genericsRepository;

    public GenericsResource(GenericsRepository genericsRepository) {
        this.genericsRepository = genericsRepository;
    }

    /**
     * {@code POST  /generics} : Create a new generics.
     *
     * @param generics the generics to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new generics, or with status {@code 400 (Bad Request)} if the generics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/generics")
    public Mono<ResponseEntity<Generics>> createGenerics(@Valid @RequestBody Generics generics) throws URISyntaxException {
        log.debug("REST request to save Generics : {}", generics);
        if (generics.getId() != null) {
            throw new BadRequestAlertException("A new generics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return genericsRepository
            .save(generics)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/generics/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /generics/:id} : Updates an existing generics.
     *
     * @param id the id of the generics to save.
     * @param generics the generics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generics,
     * or with status {@code 400 (Bad Request)} if the generics is not valid,
     * or with status {@code 500 (Internal Server Error)} if the generics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/generics/{id}")
    public Mono<ResponseEntity<Generics>> updateGenerics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Generics generics
    ) throws URISyntaxException {
        log.debug("REST request to update Generics : {}, {}", id, generics);
        if (generics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return genericsRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return genericsRepository
                        .save(generics)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            result ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString())
                                    )
                                    .body(result)
                        );
                }
            );
    }

    /**
     * {@code PATCH  /generics/:id} : Partial updates given fields of an existing generics, field will ignore if it is null
     *
     * @param id the id of the generics to save.
     * @param generics the generics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generics,
     * or with status {@code 400 (Bad Request)} if the generics is not valid,
     * or with status {@code 404 (Not Found)} if the generics is not found,
     * or with status {@code 500 (Internal Server Error)} if the generics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/generics/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Generics>> partialUpdateGenerics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Generics generics
    ) throws URISyntaxException {
        log.debug("REST request to partial update Generics partially : {}, {}", id, generics);
        if (generics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return genericsRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Generics> result = genericsRepository
                        .findById(generics.getId())
                        .map(
                            existingGenerics -> {
                                if (generics.getDosage() != null) {
                                    existingGenerics.setDosage(generics.getDosage());
                                }
                                if (generics.getDosageunit() != null) {
                                    existingGenerics.setDosageunit(generics.getDosageunit());
                                }

                                return existingGenerics;
                            }
                        )
                        .flatMap(genericsRepository::save);

                    return result
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            res ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString())
                                    )
                                    .body(res)
                        );
                }
            );
    }

    /**
     * {@code GET  /generics} : get all the generics.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of generics in body.
     */
    @GetMapping("/generics")
    public Mono<ResponseEntity<List<Generics>>> getAllGenerics(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Generics");
        return genericsRepository
            .count()
            .zipWith(genericsRepository.findAllBy(pageable).collectList())
            .map(
                countWithEntities -> {
                    return ResponseEntity
                        .ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                UriComponentsBuilder.fromHttpRequest(request),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2());
                }
            );
    }

    /**
     * {@code GET  /generics/:id} : get the "id" generics.
     *
     * @param id the id of the generics to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the generics, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/generics/{id}")
    public Mono<ResponseEntity<Generics>> getGenerics(@PathVariable Long id) {
        log.debug("REST request to get Generics : {}", id);
        Mono<Generics> generics = genericsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(generics);
    }

    /**
     * {@code DELETE  /generics/:id} : delete the "id" generics.
     *
     * @param id the id of the generics to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/generics/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteGenerics(@PathVariable Long id) {
        log.debug("REST request to delete Generics : {}", id);
        return genericsRepository
            .deleteById(id)
            .map(
                result ->
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
            );
    }
}

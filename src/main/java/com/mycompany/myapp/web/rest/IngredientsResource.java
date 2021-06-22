package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Ingredients;
import com.mycompany.myapp.repository.IngredientsRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Ingredients}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IngredientsResource {

    private final Logger log = LoggerFactory.getLogger(IngredientsResource.class);

    private static final String ENTITY_NAME = "ingredients";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngredientsRepository ingredientsRepository;

    public IngredientsResource(IngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    /**
     * {@code POST  /ingredients} : Create a new ingredients.
     *
     * @param ingredients the ingredients to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingredients, or with status {@code 400 (Bad Request)} if the ingredients has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ingredients")
    public Mono<ResponseEntity<Ingredients>> createIngredients(@Valid @RequestBody Ingredients ingredients) throws URISyntaxException {
        log.debug("REST request to save Ingredients : {}", ingredients);
        if (ingredients.getId() != null) {
            throw new BadRequestAlertException("A new ingredients cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ingredientsRepository
            .save(ingredients)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/ingredients/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /ingredients/:id} : Updates an existing ingredients.
     *
     * @param id the id of the ingredients to save.
     * @param ingredients the ingredients to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredients,
     * or with status {@code 400 (Bad Request)} if the ingredients is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingredients couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingredients/{id}")
    public Mono<ResponseEntity<Ingredients>> updateIngredients(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Ingredients ingredients
    ) throws URISyntaxException {
        log.debug("REST request to update Ingredients : {}, {}", id, ingredients);
        if (ingredients.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredients.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ingredientsRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return ingredientsRepository
                        .save(ingredients)
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
     * {@code PATCH  /ingredients/:id} : Partial updates given fields of an existing ingredients, field will ignore if it is null
     *
     * @param id the id of the ingredients to save.
     * @param ingredients the ingredients to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredients,
     * or with status {@code 400 (Bad Request)} if the ingredients is not valid,
     * or with status {@code 404 (Not Found)} if the ingredients is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingredients couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ingredients/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Ingredients>> partialUpdateIngredients(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ingredients ingredients
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ingredients partially : {}, {}", id, ingredients);
        if (ingredients.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredients.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ingredientsRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Ingredients> result = ingredientsRepository
                        .findById(ingredients.getId())
                        .map(
                            existingIngredients -> {
                                if (ingredients.getIname() != null) {
                                    existingIngredients.setIname(ingredients.getIname());
                                }
                                if (ingredients.getSymptoms() != null) {
                                    existingIngredients.setSymptoms(ingredients.getSymptoms());
                                }
                                if (ingredients.getSideeffects() != null) {
                                    existingIngredients.setSideeffects(ingredients.getSideeffects());
                                }
                                if (ingredients.getCautions() != null) {
                                    existingIngredients.setCautions(ingredients.getCautions());
                                }

                                return existingIngredients;
                            }
                        )
                        .flatMap(ingredientsRepository::save);

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
     * {@code GET  /ingredients} : get all the ingredients.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingredients in body.
     */
    @GetMapping("/ingredients")
    public Mono<ResponseEntity<List<Ingredients>>> getAllIngredients(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Ingredients");
        return ingredientsRepository
            .count()
            .zipWith(ingredientsRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /ingredients/:id} : get the "id" ingredients.
     *
     * @param id the id of the ingredients to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingredients, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingredients/{id}")
    public Mono<ResponseEntity<Ingredients>> getIngredients(@PathVariable Long id) {
        log.debug("REST request to get Ingredients : {}", id);
        Mono<Ingredients> ingredients = ingredientsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ingredients);
    }

    /**
     * {@code DELETE  /ingredients/:id} : delete the "id" ingredients.
     *
     * @param id the id of the ingredients to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingredients/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteIngredients(@PathVariable Long id) {
        log.debug("REST request to delete Ingredients : {}", id);
        return ingredientsRepository
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

package com.promition.drugwiki.web.rest;

import com.promition.drugwiki.repository.BrandRepository;
import com.promition.drugwiki.service.BrandService;
import com.promition.drugwiki.service.dto.BrandDTO;
import com.promition.drugwiki.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.promition.drugwiki.domain.Brand}.
 */
@RestController
@RequestMapping("/api")
public class BrandResource {

    private final Logger log = LoggerFactory.getLogger(BrandResource.class);

    private static final String ENTITY_NAME = "brand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BrandService brandService;

    private final BrandRepository brandRepository;

    public BrandResource(BrandService brandService, BrandRepository brandRepository) {
        this.brandService = brandService;
        this.brandRepository = brandRepository;
    }

    /**
     * {@code POST  /brands} : Create a new brand.
     *
     * @param brandDTO the brandDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new brandDTO, or with status {@code 400 (Bad Request)} if the brand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/brands")
    public Mono<ResponseEntity<BrandDTO>> createBrand(@Valid @RequestBody BrandDTO brandDTO) throws URISyntaxException {
        log.debug("REST request to save Brand : {}", brandDTO);
        if (brandDTO.getId() != null) {
            throw new BadRequestAlertException("A new brand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return brandService
            .save(brandDTO)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/brands/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /brands/:id} : Updates an existing brand.
     *
     * @param id the id of the brandDTO to save.
     * @param brandDTO the brandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brandDTO,
     * or with status {@code 400 (Bad Request)} if the brandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the brandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/brands/{id}")
    public Mono<ResponseEntity<BrandDTO>> updateBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BrandDTO brandDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Brand : {}, {}", id, brandDTO);
        if (brandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, brandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return brandRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return brandService
                        .save(brandDTO)
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
     * {@code PATCH  /brands/:id} : Partial updates given fields of an existing brand, field will ignore if it is null
     *
     * @param id the id of the brandDTO to save.
     * @param brandDTO the brandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brandDTO,
     * or with status {@code 400 (Bad Request)} if the brandDTO is not valid,
     * or with status {@code 404 (Not Found)} if the brandDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the brandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/brands/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<BrandDTO>> partialUpdateBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BrandDTO brandDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Brand partially : {}, {}", id, brandDTO);
        if (brandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, brandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return brandRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<BrandDTO> result = brandService.partialUpdate(brandDTO);

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
     * {@code GET  /brands} : get all the brands.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of brands in body.
     */
    @GetMapping("/brands")
    public Mono<List<BrandDTO>> getAllBrands() {
        log.debug("REST request to get all Brands");
        return brandService.findAll().collectList();
    }

    /**
     * {@code GET  /brands} : get all the brands as a stream.
     * @return the {@link Flux} of brands.
     */
    @GetMapping(value = "/brands", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<BrandDTO> getAllBrandsAsStream() {
        log.debug("REST request to get all Brands as a stream");
        return brandService.findAll();
    }

    /**
     * {@code GET  /brands/:id} : get the "id" brand.
     *
     * @param id the id of the brandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the brandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/brands/{id}")
    public Mono<ResponseEntity<BrandDTO>> getBrand(@PathVariable Long id) {
        log.debug("REST request to get Brand : {}", id);
        Mono<BrandDTO> brandDTO = brandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brandDTO);
    }

    /**
     * {@code DELETE  /brands/:id} : delete the "id" brand.
     *
     * @param id the id of the brandDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/brands/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBrand(@PathVariable Long id) {
        log.debug("REST request to delete Brand : {}", id);
        return brandService
            .delete(id)
            .map(
                result ->
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
            );
    }
}

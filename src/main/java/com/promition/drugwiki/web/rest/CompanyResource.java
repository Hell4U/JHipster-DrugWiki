package com.promition.drugwiki.web.rest;

import com.promition.drugwiki.repository.CompanyRepository;
import com.promition.drugwiki.service.CompanyService;
import com.promition.drugwiki.service.dto.CompanyDTO;
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
 * REST controller for managing {@link com.promition.drugwiki.domain.Company}.
 */
@RestController
@RequestMapping("/api")
public class CompanyResource {

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    private static final String ENTITY_NAME = "company";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyService companyService;

    private final CompanyRepository companyRepository;

    public CompanyResource(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }

    /**
     * {@code POST  /companies} : Create a new company.
     *
     * @param companyDTO the companyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyDTO, or with status {@code 400 (Bad Request)} if the company has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/companies")
    public Mono<ResponseEntity<CompanyDTO>> createCompany(@Valid @RequestBody CompanyDTO companyDTO) throws URISyntaxException {
        log.debug("REST request to save Company : {}", companyDTO);
        if (companyDTO.getId() != null) {
            throw new BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return companyService
            .save(companyDTO)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/companies/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /companies/:id} : Updates an existing company.
     *
     * @param id the id of the companyDTO to save.
     * @param companyDTO the companyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDTO,
     * or with status {@code 400 (Bad Request)} if the companyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/companies/{id}")
    public Mono<ResponseEntity<CompanyDTO>> updateCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompanyDTO companyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Company : {}, {}", id, companyDTO);
        if (companyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return companyRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return companyService
                        .save(companyDTO)
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
     * {@code PATCH  /companies/:id} : Partial updates given fields of an existing company, field will ignore if it is null
     *
     * @param id the id of the companyDTO to save.
     * @param companyDTO the companyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDTO,
     * or with status {@code 400 (Bad Request)} if the companyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/companies/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<CompanyDTO>> partialUpdateCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompanyDTO companyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Company partially : {}, {}", id, companyDTO);
        if (companyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return companyRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<CompanyDTO> result = companyService.partialUpdate(companyDTO);

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
     * {@code GET  /companies} : get all the companies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companies in body.
     */
    @GetMapping("/companies")
    public Mono<List<CompanyDTO>> getAllCompanies() {
        log.debug("REST request to get all Companies");
        return companyService.findAll().collectList();
    }

    /**
     * {@code GET  /companies} : get all the companies as a stream.
     * @return the {@link Flux} of companies.
     */
    @GetMapping(value = "/companies", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CompanyDTO> getAllCompaniesAsStream() {
        log.debug("REST request to get all Companies as a stream");
        return companyService.findAll();
    }

    /**
     * {@code GET  /companies/:id} : get the "id" company.
     *
     * @param id the id of the companyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/companies/{id}")
    public Mono<ResponseEntity<CompanyDTO>> getCompany(@PathVariable Long id) {
        log.debug("REST request to get Company : {}", id);
        Mono<CompanyDTO> companyDTO = companyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyDTO);
    }

    /**
     * {@code DELETE  /companies/:id} : delete the "id" company.
     *
     * @param id the id of the companyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/companies/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCompany(@PathVariable Long id) {
        log.debug("REST request to delete Company : {}", id);
        return companyService
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

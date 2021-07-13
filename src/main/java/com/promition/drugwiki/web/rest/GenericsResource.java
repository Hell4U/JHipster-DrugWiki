package com.promition.drugwiki.web.rest;

import com.promition.drugwiki.repository.GenericsRepository;
import com.promition.drugwiki.service.GenericsQueryService;
import com.promition.drugwiki.service.GenericsService;
import com.promition.drugwiki.service.criteria.GenericsCriteria;
import com.promition.drugwiki.service.dto.GenericsDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.promition.drugwiki.domain.Generics}.
 */
@RestController
@RequestMapping("/api")
public class GenericsResource {

    private final Logger log = LoggerFactory.getLogger(GenericsResource.class);

    private static final String ENTITY_NAME = "generics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenericsService genericsService;

    private final GenericsRepository genericsRepository;

    private final GenericsQueryService genericsQueryService;

    public GenericsResource(
        GenericsService genericsService,
        GenericsRepository genericsRepository,
        GenericsQueryService genericsQueryService
    ) {
        this.genericsService = genericsService;
        this.genericsRepository = genericsRepository;
        this.genericsQueryService = genericsQueryService;
    }

    /**
     * {@code POST  /generics} : Create a new generics.
     *
     * @param genericsDTO the genericsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new genericsDTO, or with status {@code 400 (Bad Request)} if the generics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/generics")
    public ResponseEntity<GenericsDTO> createGenerics(@Valid @RequestBody GenericsDTO genericsDTO) throws URISyntaxException {
        log.debug("REST request to save Generics : {}", genericsDTO);
        if (genericsDTO.getId() != null) {
            throw new BadRequestAlertException("A new generics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenericsDTO result = genericsService.save(genericsDTO);
        return ResponseEntity
            .created(new URI("/api/generics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /generics/:id} : Updates an existing generics.
     *
     * @param id the id of the genericsDTO to save.
     * @param genericsDTO the genericsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genericsDTO,
     * or with status {@code 400 (Bad Request)} if the genericsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genericsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/generics/{id}")
    public ResponseEntity<GenericsDTO> updateGenerics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GenericsDTO genericsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Generics : {}, {}", id, genericsDTO);
        if (genericsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genericsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genericsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GenericsDTO result = genericsService.save(genericsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genericsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /generics/:id} : Partial updates given fields of an existing generics, field will ignore if it is null
     *
     * @param id the id of the genericsDTO to save.
     * @param genericsDTO the genericsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genericsDTO,
     * or with status {@code 400 (Bad Request)} if the genericsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the genericsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the genericsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/generics/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GenericsDTO> partialUpdateGenerics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GenericsDTO genericsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Generics partially : {}, {}", id, genericsDTO);
        if (genericsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genericsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genericsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GenericsDTO> result = genericsService.partialUpdate(genericsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genericsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /generics} : get all the generics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of generics in body.
     */
    @GetMapping("/generics")
    public ResponseEntity<List<GenericsDTO>> getAllGenerics(GenericsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Generics by criteria: {}", criteria);
        Page<GenericsDTO> page = genericsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /generics/count} : count all the generics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/generics/count")
    public ResponseEntity<Long> countGenerics(GenericsCriteria criteria) {
        log.debug("REST request to count Generics by criteria: {}", criteria);
        return ResponseEntity.ok().body(genericsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /generics/:id} : get the "id" generics.
     *
     * @param id the id of the genericsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the genericsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/generics/{id}")
    public ResponseEntity<GenericsDTO> getGenerics(@PathVariable Long id) {
        log.debug("REST request to get Generics : {}", id);
        Optional<GenericsDTO> genericsDTO = genericsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genericsDTO);
    }

    /**
     * {@code DELETE  /generics/:id} : delete the "id" generics.
     *
     * @param id the id of the genericsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/generics/{id}")
    public ResponseEntity<Void> deleteGenerics(@PathVariable Long id) {
        log.debug("REST request to delete Generics : {}", id);
        genericsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

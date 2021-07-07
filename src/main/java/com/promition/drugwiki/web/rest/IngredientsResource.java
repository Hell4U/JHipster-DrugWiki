package com.promition.drugwiki.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.promition.drugwiki.repository.IngredientsRepository;
import com.promition.drugwiki.service.IngredientsQueryService;
import com.promition.drugwiki.service.IngredientsService;
import com.promition.drugwiki.service.criteria.IngredientsCriteria;
import com.promition.drugwiki.service.dto.IngredientsDTO;
import com.promition.drugwiki.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.promition.drugwiki.domain.Ingredients}.
 */
@RestController
@RequestMapping("/api")
public class IngredientsResource {

    private final Logger log = LoggerFactory.getLogger(IngredientsResource.class);

    private static final String ENTITY_NAME = "ingredients";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngredientsService ingredientsService;

    private final IngredientsRepository ingredientsRepository;

    private final IngredientsQueryService ingredientsQueryService;

    public IngredientsResource(
        IngredientsService ingredientsService,
        IngredientsRepository ingredientsRepository,
        IngredientsQueryService ingredientsQueryService
    ) {
        this.ingredientsService = ingredientsService;
        this.ingredientsRepository = ingredientsRepository;
        this.ingredientsQueryService = ingredientsQueryService;
    }

    /**
     * {@code POST  /ingredients} : Create a new ingredients.
     *
     * @param ingredientsDTO the ingredientsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingredientsDTO, or with status {@code 400 (Bad Request)} if the ingredients has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ingredients")
    public ResponseEntity<IngredientsDTO> createIngredients(@Valid @RequestBody IngredientsDTO ingredientsDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredients : {}", ingredientsDTO);
        if (ingredientsDTO.getId() != null) {
            throw new BadRequestAlertException("A new ingredients cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IngredientsDTO result = ingredientsService.save(ingredientsDTO);
        return ResponseEntity
            .created(new URI("/api/ingredients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ingredients/:id} : Updates an existing ingredients.
     *
     * @param id the id of the ingredientsDTO to save.
     * @param ingredientsDTO the ingredientsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredientsDTO,
     * or with status {@code 400 (Bad Request)} if the ingredientsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingredientsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingredients/{id}")
    public ResponseEntity<IngredientsDTO> updateIngredients(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IngredientsDTO ingredientsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ingredients : {}, {}", id, ingredientsDTO);
        if (ingredientsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredientsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IngredientsDTO result = ingredientsService.save(ingredientsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ingredientsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ingredients/:id} : Partial updates given fields of an existing ingredients, field will ignore if it is null
     *
     * @param id the id of the ingredientsDTO to save.
     * @param ingredientsDTO the ingredientsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredientsDTO,
     * or with status {@code 400 (Bad Request)} if the ingredientsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ingredientsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingredientsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ingredients/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<IngredientsDTO> partialUpdateIngredients(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IngredientsDTO ingredientsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ingredients partially : {}, {}", id, ingredientsDTO);
        if (ingredientsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredientsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IngredientsDTO> result = ingredientsService.partialUpdate(ingredientsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ingredientsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ingredients} : get all the ingredients.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingredients in body.
     */
    @GetMapping("/ingredients")
    public ResponseEntity<List<IngredientsDTO>> getAllIngredients(IngredientsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ingredients by criteria: {}", criteria);
        Page<IngredientsDTO> page = ingredientsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ingredients/count} : count all the ingredients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ingredients/count")
    public ResponseEntity<Long> countIngredients(IngredientsCriteria criteria) {
        log.debug("REST request to count Ingredients by criteria: {}", criteria);
        return ResponseEntity.ok().body(ingredientsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ingredients/:id} : get the "id" ingredients.
     *
     * @param id the id of the ingredientsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingredientsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingredients/{id}")
    public ResponseEntity<IngredientsDTO> getIngredients(@PathVariable Long id) {
        log.debug("REST request to get Ingredients : {}", id);
        Optional<IngredientsDTO> ingredientsDTO = ingredientsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingredientsDTO);
    }

    /**
     * {@code DELETE  /ingredients/:id} : delete the "id" ingredients.
     *
     * @param id the id of the ingredientsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<Void> deleteIngredients(@PathVariable Long id) {
        log.debug("REST request to delete Ingredients : {}", id);
        ingredientsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ingredients?query=:query} : search for the ingredients corresponding
     * to the query.
     *
     * @param query the query of the ingredients search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ingredients")
    public ResponseEntity<List<IngredientsDTO>> searchIngredients(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Ingredients for query {}", query);
        Page<IngredientsDTO> page = ingredientsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}

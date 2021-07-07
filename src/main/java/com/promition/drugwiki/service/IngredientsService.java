package com.promition.drugwiki.service;

import com.promition.drugwiki.service.dto.IngredientsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.promition.drugwiki.domain.Ingredients}.
 */
public interface IngredientsService {
    /**
     * Save a ingredients.
     *
     * @param ingredientsDTO the entity to save.
     * @return the persisted entity.
     */
    IngredientsDTO save(IngredientsDTO ingredientsDTO);

    /**
     * Partially updates a ingredients.
     *
     * @param ingredientsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IngredientsDTO> partialUpdate(IngredientsDTO ingredientsDTO);

    /**
     * Get all the ingredients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IngredientsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ingredients.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IngredientsDTO> findOne(Long id);

    /**
     * Delete the "id" ingredients.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the ingredients corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IngredientsDTO> search(String query, Pageable pageable);
}

package com.promition.drugwiki.service;

import com.promition.drugwiki.service.dto.GenericsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.promition.drugwiki.domain.Generics}.
 */
public interface GenericsService {
    /**
     * Save a generics.
     *
     * @param genericsDTO the entity to save.
     * @return the persisted entity.
     */
    GenericsDTO save(GenericsDTO genericsDTO);

    /**
     * Partially updates a generics.
     *
     * @param genericsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GenericsDTO> partialUpdate(GenericsDTO genericsDTO);

    /**
     * Get all the generics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GenericsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" generics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GenericsDTO> findOne(Long id);

    /**
     * Delete the "id" generics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the generics corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GenericsDTO> search(String query, Pageable pageable);
}

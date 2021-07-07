package com.promition.drugwiki.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.promition.drugwiki.domain.Ingredients;
import com.promition.drugwiki.repository.IngredientsRepository;
import com.promition.drugwiki.repository.search.IngredientsSearchRepository;
import com.promition.drugwiki.service.IngredientsService;
import com.promition.drugwiki.service.dto.IngredientsDTO;
import com.promition.drugwiki.service.mapper.IngredientsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ingredients}.
 */
@Service
@Transactional
public class IngredientsServiceImpl implements IngredientsService {

    private final Logger log = LoggerFactory.getLogger(IngredientsServiceImpl.class);

    private final IngredientsRepository ingredientsRepository;

    private final IngredientsMapper ingredientsMapper;

    private final IngredientsSearchRepository ingredientsSearchRepository;

    public IngredientsServiceImpl(
        IngredientsRepository ingredientsRepository,
        IngredientsMapper ingredientsMapper,
        IngredientsSearchRepository ingredientsSearchRepository
    ) {
        this.ingredientsRepository = ingredientsRepository;
        this.ingredientsMapper = ingredientsMapper;
        this.ingredientsSearchRepository = ingredientsSearchRepository;
    }

    @Override
    public IngredientsDTO save(IngredientsDTO ingredientsDTO) {
        log.debug("Request to save Ingredients : {}", ingredientsDTO);
        Ingredients ingredients = ingredientsMapper.toEntity(ingredientsDTO);
        ingredients = ingredientsRepository.save(ingredients);
        IngredientsDTO result = ingredientsMapper.toDto(ingredients);
        ingredientsSearchRepository.save(ingredients);
        return result;
    }

    @Override
    public Optional<IngredientsDTO> partialUpdate(IngredientsDTO ingredientsDTO) {
        log.debug("Request to partially update Ingredients : {}", ingredientsDTO);

        return ingredientsRepository
            .findById(ingredientsDTO.getId())
            .map(
                existingIngredients -> {
                    ingredientsMapper.partialUpdate(existingIngredients, ingredientsDTO);

                    return existingIngredients;
                }
            )
            .map(ingredientsRepository::save)
            .map(
                savedIngredients -> {
                    ingredientsSearchRepository.save(savedIngredients);

                    return savedIngredients;
                }
            )
            .map(ingredientsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IngredientsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ingredients");
        return ingredientsRepository.findAll(pageable).map(ingredientsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngredientsDTO> findOne(Long id) {
        log.debug("Request to get Ingredients : {}", id);
        return ingredientsRepository.findById(id).map(ingredientsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ingredients : {}", id);
        ingredientsRepository.deleteById(id);
        ingredientsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IngredientsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ingredients for query {}", query);
        return ingredientsSearchRepository.search(queryStringQuery(query), pageable).map(ingredientsMapper::toDto);
    }
}

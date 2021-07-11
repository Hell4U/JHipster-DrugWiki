package com.promition.drugwiki.service;

import com.promition.drugwiki.domain.*; // for static metamodels
import com.promition.drugwiki.domain.Ingredients;
import com.promition.drugwiki.repository.IngredientsRepository;
import com.promition.drugwiki.service.criteria.IngredientsCriteria;
import com.promition.drugwiki.service.dto.IngredientsDTO;
import com.promition.drugwiki.service.mapper.IngredientsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Ingredients} entities in the database.
 * The main input is a {@link IngredientsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IngredientsDTO} or a {@link Page} of {@link IngredientsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IngredientsQueryService extends QueryService<Ingredients> {

    private final Logger log = LoggerFactory.getLogger(IngredientsQueryService.class);

    private final IngredientsRepository ingredientsRepository;

    private final IngredientsMapper ingredientsMapper;

    public IngredientsQueryService(IngredientsRepository ingredientsRepository, IngredientsMapper ingredientsMapper) {
        this.ingredientsRepository = ingredientsRepository;
        this.ingredientsMapper = ingredientsMapper;
    }

    /**
     * Return a {@link List} of {@link IngredientsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IngredientsDTO> findByCriteria(IngredientsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ingredients> specification = createSpecification(criteria);
        return ingredientsMapper.toDto(ingredientsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IngredientsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IngredientsDTO> findByCriteria(IngredientsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ingredients> specification = createSpecification(criteria);
        return ingredientsRepository.findAll(specification, page).map(ingredientsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IngredientsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ingredients> specification = createSpecification(criteria);
        return ingredientsRepository.count(specification);
    }

    /**
     * Function to convert {@link IngredientsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ingredients> createSpecification(IngredientsCriteria criteria) {
        Specification<Ingredients> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ingredients_.id));
            }
            if (criteria.getIname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIname(), Ingredients_.iname));
            }
            if (criteria.getSymptoms() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSymptoms(), Ingredients_.symptoms));
            }
            if (criteria.getSideeffects() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSideeffects(), Ingredients_.sideeffects));
            }
            if (criteria.getCautions() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCautions(), Ingredients_.cautions));
            }
        }
        return specification;
    }
}

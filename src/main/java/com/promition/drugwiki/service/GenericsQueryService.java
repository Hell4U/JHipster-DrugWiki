package com.promition.drugwiki.service;

import com.promition.drugwiki.domain.*; // for static metamodels
import com.promition.drugwiki.domain.Generics;
import com.promition.drugwiki.repository.GenericsRepository;
import com.promition.drugwiki.repository.search.GenericsSearchRepository;
import com.promition.drugwiki.service.criteria.GenericsCriteria;
import com.promition.drugwiki.service.dto.GenericsDTO;
import com.promition.drugwiki.service.mapper.GenericsMapper;
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
 * Service for executing complex queries for {@link Generics} entities in the database.
 * The main input is a {@link GenericsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GenericsDTO} or a {@link Page} of {@link GenericsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GenericsQueryService extends QueryService<Generics> {

    private final Logger log = LoggerFactory.getLogger(GenericsQueryService.class);

    private final GenericsRepository genericsRepository;

    private final GenericsMapper genericsMapper;

    private final GenericsSearchRepository genericsSearchRepository;

    public GenericsQueryService(
        GenericsRepository genericsRepository,
        GenericsMapper genericsMapper,
        GenericsSearchRepository genericsSearchRepository
    ) {
        this.genericsRepository = genericsRepository;
        this.genericsMapper = genericsMapper;
        this.genericsSearchRepository = genericsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GenericsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GenericsDTO> findByCriteria(GenericsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Generics> specification = createSpecification(criteria);
        return genericsMapper.toDto(genericsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GenericsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GenericsDTO> findByCriteria(GenericsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Generics> specification = createSpecification(criteria);
        return genericsRepository.findAll(specification, page).map(genericsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GenericsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Generics> specification = createSpecification(criteria);
        return genericsRepository.count(specification);
    }

    /**
     * Function to convert {@link GenericsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Generics> createSpecification(GenericsCriteria criteria) {
        Specification<Generics> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Generics_.id));
            }
            if (criteria.getGname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGname(), Generics_.gname));
            }
            if (criteria.getDosage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDosage(), Generics_.dosage));
            }
            if (criteria.getDosageunit() != null) {
                specification = specification.and(buildSpecification(criteria.getDosageunit(), Generics_.dosageunit));
            }
            if (criteria.getIngredientsusedId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIngredientsusedId(),
                            root -> root.join(Generics_.ingredientsused, JoinType.LEFT).get(Ingredients_.id)
                        )
                    );
            }
            if (criteria.getIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getIdId(), root -> root.join(Generics_.ids, JoinType.LEFT).get(Brand_.id))
                    );
            }
        }
        return specification;
    }
}

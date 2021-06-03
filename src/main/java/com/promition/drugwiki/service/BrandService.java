package com.promition.drugwiki.service;

import com.promition.drugwiki.domain.Brand;
import com.promition.drugwiki.repository.BrandRepository;
import com.promition.drugwiki.service.dto.BrandDTO;
import com.promition.drugwiki.service.mapper.BrandMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Brand}.
 */
@Service
@Transactional
public class BrandService {

    private final Logger log = LoggerFactory.getLogger(BrandService.class);

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public BrandService(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    /**
     * Save a brand.
     *
     * @param brandDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<BrandDTO> save(BrandDTO brandDTO) {
        log.debug("Request to save Brand : {}", brandDTO);
        return brandRepository.save(brandMapper.toEntity(brandDTO)).map(brandMapper::toDto);
    }

    /**
     * Partially update a brand.
     *
     * @param brandDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<BrandDTO> partialUpdate(BrandDTO brandDTO) {
        log.debug("Request to partially update Brand : {}", brandDTO);

        return brandRepository
            .findById(brandDTO.getId())
            .map(
                existingBrand -> {
                    brandMapper.partialUpdate(existingBrand, brandDTO);
                    return existingBrand;
                }
            )
            .flatMap(brandRepository::save)
            .map(brandMapper::toDto);
    }

    /**
     * Get all the brands.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<BrandDTO> findAll() {
        log.debug("Request to get all Brands");
        return brandRepository.findAll().map(brandMapper::toDto);
    }

    /**
     * Returns the number of brands available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return brandRepository.count();
    }

    /**
     * Get one brand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<BrandDTO> findOne(Long id) {
        log.debug("Request to get Brand : {}", id);
        return brandRepository.findById(id).map(brandMapper::toDto);
    }

    /**
     * Delete the brand by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Brand : {}", id);
        return brandRepository.deleteById(id);
    }
}

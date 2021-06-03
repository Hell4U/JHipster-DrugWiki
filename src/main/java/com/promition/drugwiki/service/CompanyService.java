package com.promition.drugwiki.service;

import com.promition.drugwiki.domain.Company;
import com.promition.drugwiki.repository.CompanyRepository;
import com.promition.drugwiki.service.dto.CompanyDTO;
import com.promition.drugwiki.service.mapper.CompanyMapper;
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
 * Service Implementation for managing {@link Company}.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        return companyRepository.save(companyMapper.toEntity(companyDTO)).map(companyMapper::toDto);
    }

    /**
     * Partially update a company.
     *
     * @param companyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CompanyDTO> partialUpdate(CompanyDTO companyDTO) {
        log.debug("Request to partially update Company : {}", companyDTO);

        return companyRepository
            .findById(companyDTO.getId())
            .map(
                existingCompany -> {
                    companyMapper.partialUpdate(existingCompany, companyDTO);
                    return existingCompany;
                }
            )
            .flatMap(companyRepository::save)
            .map(companyMapper::toDto);
    }

    /**
     * Get all the companies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CompanyDTO> findAll() {
        log.debug("Request to get all Companies");
        return companyRepository.findAll().map(companyMapper::toDto);
    }

    /**
     * Returns the number of companies available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return companyRepository.count();
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CompanyDTO> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id).map(companyMapper::toDto);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        return companyRepository.deleteById(id);
    }
}

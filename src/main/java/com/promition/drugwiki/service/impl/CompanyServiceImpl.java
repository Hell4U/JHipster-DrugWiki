package com.promition.drugwiki.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.promition.drugwiki.domain.Company;
import com.promition.drugwiki.repository.CompanyRepository;
import com.promition.drugwiki.repository.search.CompanySearchRepository;
import com.promition.drugwiki.service.CompanyService;
import com.promition.drugwiki.service.dto.CompanyDTO;
import com.promition.drugwiki.service.mapper.CompanyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Company}.
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    private final CompanySearchRepository companySearchRepository;

    public CompanyServiceImpl(
        CompanyRepository companyRepository,
        CompanyMapper companyMapper,
        CompanySearchRepository companySearchRepository
    ) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.companySearchRepository = companySearchRepository;
    }

    @Override
    public CompanyDTO save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        company = companyRepository.save(company);
        CompanyDTO result = companyMapper.toDto(company);
        companySearchRepository.save(company);
        return result;
    }

    @Override
    public Optional<CompanyDTO> partialUpdate(CompanyDTO companyDTO) {
        log.debug("Request to partially update Company : {}", companyDTO);

        return companyRepository
            .findById(companyDTO.getId())
            .map(
                existingCompany -> {
                    companyMapper.partialUpdate(existingCompany, companyDTO);

                    return existingCompany;
                }
            )
            .map(companyRepository::save)
            .map(
                savedCompany -> {
                    companySearchRepository.save(savedCompany);

                    return savedCompany;
                }
            )
            .map(companyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAll(pageable).map(companyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyDTO> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id).map(companyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
        companySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Companies for query {}", query);
        return companySearchRepository.search(queryStringQuery(query), pageable).map(companyMapper::toDto);
    }
}

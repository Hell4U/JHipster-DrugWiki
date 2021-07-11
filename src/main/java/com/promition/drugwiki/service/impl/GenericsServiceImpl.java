package com.promition.drugwiki.service.impl;

import com.promition.drugwiki.domain.Generics;
import com.promition.drugwiki.repository.GenericsRepository;
import com.promition.drugwiki.service.GenericsService;
import com.promition.drugwiki.service.dto.GenericsDTO;
import com.promition.drugwiki.service.mapper.GenericsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Generics}.
 */
@Service
@Transactional
public class GenericsServiceImpl implements GenericsService {

    private final Logger log = LoggerFactory.getLogger(GenericsServiceImpl.class);

    private final GenericsRepository genericsRepository;

    private final GenericsMapper genericsMapper;

    public GenericsServiceImpl(GenericsRepository genericsRepository, GenericsMapper genericsMapper) {
        this.genericsRepository = genericsRepository;
        this.genericsMapper = genericsMapper;
    }

    @Override
    public GenericsDTO save(GenericsDTO genericsDTO) {
        log.debug("Request to save Generics : {}", genericsDTO);
        Generics generics = genericsMapper.toEntity(genericsDTO);
        generics = genericsRepository.save(generics);
        return genericsMapper.toDto(generics);
    }

    @Override
    public Optional<GenericsDTO> partialUpdate(GenericsDTO genericsDTO) {
        log.debug("Request to partially update Generics : {}", genericsDTO);

        return genericsRepository
            .findById(genericsDTO.getId())
            .map(
                existingGenerics -> {
                    genericsMapper.partialUpdate(existingGenerics, genericsDTO);

                    return existingGenerics;
                }
            )
            .map(genericsRepository::save)
            .map(genericsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenericsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Generics");
        return genericsRepository.findAll(pageable).map(genericsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenericsDTO> findOne(Long id) {
        log.debug("Request to get Generics : {}", id);
        return genericsRepository.findById(id).map(genericsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Generics : {}", id);
        genericsRepository.deleteById(id);
    }
}

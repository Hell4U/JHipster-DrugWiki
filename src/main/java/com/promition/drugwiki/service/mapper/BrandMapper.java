package com.promition.drugwiki.service.mapper;

import com.promition.drugwiki.domain.*;
import com.promition.drugwiki.service.dto.BrandDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Brand} and its DTO {@link BrandDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface BrandMapper extends EntityMapper<BrandDTO, Brand> {
    @Mapping(target = "company", source = "company", qualifiedByName = "id")
    BrandDTO toDto(Brand s);
}

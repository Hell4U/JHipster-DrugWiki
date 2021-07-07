package com.promition.drugwiki.service.mapper;

import com.promition.drugwiki.domain.*;
import com.promition.drugwiki.service.dto.BrandDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Brand} and its DTO {@link BrandDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, GenericsMapper.class })
public interface BrandMapper extends EntityMapper<BrandDTO, Brand> {
    @Mapping(target = "companyofMedicine", source = "companyofMedicine", qualifiedByName = "cname")
    @Mapping(target = "genericsuseds", source = "genericsuseds", qualifiedByName = "gnameSet")
    BrandDTO toDto(Brand s);

    @Mapping(target = "removeGenericsused", ignore = true)
    Brand toEntity(BrandDTO brandDTO);
}

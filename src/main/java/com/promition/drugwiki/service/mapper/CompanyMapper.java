package com.promition.drugwiki.service.mapper;

import com.promition.drugwiki.domain.*;
import com.promition.drugwiki.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoId(Company company);
}

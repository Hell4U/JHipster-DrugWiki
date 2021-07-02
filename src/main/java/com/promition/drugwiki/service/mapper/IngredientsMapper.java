package com.promition.drugwiki.service.mapper;

import com.promition.drugwiki.domain.*;
import com.promition.drugwiki.service.dto.IngredientsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ingredients} and its DTO {@link IngredientsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IngredientsMapper extends EntityMapper<IngredientsDTO, Ingredients> {
    @Named("iname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "iname", source = "iname")
    IngredientsDTO toDtoIname(Ingredients ingredients);
}

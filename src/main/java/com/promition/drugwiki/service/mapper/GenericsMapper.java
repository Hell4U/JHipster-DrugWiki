package com.promition.drugwiki.service.mapper;

import com.promition.drugwiki.domain.*;
import com.promition.drugwiki.service.dto.GenericsDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Generics} and its DTO {@link GenericsDTO}.
 */
@Mapper(componentModel = "spring", uses = { IngredientsMapper.class })
public interface GenericsMapper extends EntityMapper<GenericsDTO, Generics> {
    @Mapping(target = "ingredientsused", source = "ingredientsused", qualifiedByName = "iname")
    GenericsDTO toDto(Generics s);

    @Named("gnameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "gname", source = "gname")
    Set<GenericsDTO> toDtoGnameSet(Set<Generics> generics);
}

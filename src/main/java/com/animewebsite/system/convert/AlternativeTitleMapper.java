package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.lazy.AlternativeTitleDtoLazy;
import com.animewebsite.system.model.AlternativeTitle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface AlternativeTitleMapper {

    @Mapping(source = "id",target = "id")
    @Mapping(source = "alternativeName",target = "alternativeName")
    @Mapping(source = "language",target = "language")
    AlternativeTitleDtoLazy alternativeTitleToAlternativeTitleDtoLazy(AlternativeTitle alternativeTitle);

    Set<AlternativeTitleDtoLazy> alternativeTitleSetToAlternativeTitleDtoLazySet(Set<AlternativeTitle> alternativeTitles);
}

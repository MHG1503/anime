package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.detail.StudioDtoDetail;
import com.animewebsite.system.dto.res.lazy.StudioDtoLazy;
import com.animewebsite.system.model.Studio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {AnimeMapper.class, ImageMapper.class})
public interface StudioMapper {

    @Mapping(source = "image",target = "image")
    StudioDtoLazy studioToStudioDtoLazy(Studio studio);

    //todo: remove this and replace by another
    @Mapping(source = "image",target = "image")
    @Mapping(source = "animeSet",target = "animeDtoLazySet")
    StudioDtoDetail studioToStudioDtoDetail(Studio studio);
}

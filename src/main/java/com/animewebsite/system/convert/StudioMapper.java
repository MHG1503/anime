package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.lazy.StudioDtoLazy;
import com.animewebsite.system.model.Studio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {AnimeMapper.class, ImageMapper.class})
public interface StudioMapper {

    @Mapping(source = "image",target = "image")
    StudioDtoLazy studioToStudioDtoLazy(Studio studio);

}

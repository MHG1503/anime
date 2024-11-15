package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.lazy.EpisodeDtoLazy;
import com.animewebsite.system.model.Episode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = ImageMapper.class)
public interface EpisodeMapper {
    @Mapping(source = "image",target = "image")
    EpisodeDtoLazy episodeToEpisodeDtoLazy(Episode episode);
}

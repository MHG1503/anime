package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.detail.GenreDtoDetail;
import com.animewebsite.system.dto.res.lazy.GenreDtoLazy;
import com.animewebsite.system.model.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {AnimeMapper.class})
public interface GenreMapper {
    GenreDtoLazy genreToGenreDtoLazy(Genre genre);

    @Mapping(source = "animeSet",target = "animeDtoLazySet")
    GenreDtoDetail genreToGenreDtoDetail(Genre genre);
}

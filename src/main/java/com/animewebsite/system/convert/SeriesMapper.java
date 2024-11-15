package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.detail.SeriesDtoDetail;
import com.animewebsite.system.dto.res.lazy.SeriesDtoLazy;
import com.animewebsite.system.model.Series;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {AnimeMapper.class})
public interface SeriesMapper {

    SeriesDtoLazy seriesToSeriesDtoLazy(Series series);

    @Mapping(source = "animeSet",target = "animeDtoLazySet")
    SeriesDtoDetail seriesToSeriesDtoDetail(Series series);
}

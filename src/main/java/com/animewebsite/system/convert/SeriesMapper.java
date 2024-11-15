package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.detail.SeriesDtoDetail;
import com.animewebsite.system.dto.res.lazy.SeriesDtoLazy;
import com.animewebsite.system.model.Series;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeriesMapper {

    SeriesDtoLazy seriesToSeriesDtoLazy(Series series);

}

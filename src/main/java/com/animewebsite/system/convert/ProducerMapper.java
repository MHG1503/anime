package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.lazy.ProducerDtoLazy;
import com.animewebsite.system.model.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {AnimeMapper.class, ImageMapper.class})
public interface ProducerMapper {

    @Mapping(source = "image",target = "image")
    ProducerDtoLazy producerToProducerDtoLazy(Producer producer);

}

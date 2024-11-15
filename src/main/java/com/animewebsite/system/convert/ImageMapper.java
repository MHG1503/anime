package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.lazy.ImageDtoLazy;
import com.animewebsite.system.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    @Mapping(source = "imageUrl",target = "imageUrl")
    @Mapping(source = "id",target = "id")
    ImageDtoLazy imageToImageDtoLazy(Image image);

}

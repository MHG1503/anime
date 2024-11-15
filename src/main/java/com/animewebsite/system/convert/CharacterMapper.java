package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.lazy.CharacterDtoLazy;
import com.animewebsite.system.model.Character;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {ImageMapper.class})
public interface CharacterMapper{
    @Mapping(source = "image",target = "image")
    CharacterDtoLazy characterToCharacterDtoLazy(Character character);

}

package com.animewebsite.system.convert;


import com.animewebsite.system.dto.res.view.CharacterVoiceActorRoleViewDto;
import com.animewebsite.system.dto.res.view.VoiceActorViewDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {
        VoiceActorViewDto.class,
})
public interface CharacterVoiceActorMapper {


    CharacterVoiceActorRoleViewDto objectToCharacterVoiceActorRoleViewDto(Object object);
}

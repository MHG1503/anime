package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.NationalityResponse;
import com.animewebsite.system.dto.res.lazy.VoiceActorDtoLazy;
import com.animewebsite.system.dto.res.select.VoiceActorDtoSelection;
import com.animewebsite.system.model.VoiceActor;
import com.animewebsite.system.model.enums.Nationality;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",uses = {ImageMapper.class})
public interface VoiceActorMapper {

    @Mapping(source = "image",target = "image")
    @Mapping(source = "nationality",target = "nationality",qualifiedByName = "toNationalityRes")
    VoiceActorDtoLazy voiceActorToVoiceActorDtoLazy(VoiceActor voiceActor);

    @Mapping(source = "image",target = "image")
    @Mapping(source = "name",target = "name")
    @Mapping(source = "id",target = "id")
    VoiceActorDtoSelection voiceActorToVoiceActorDtoSelect(VoiceActor voiceActor);

    @Named("toNationalityRes")
    default NationalityResponse toNationalityRes(Nationality nationality){
        return NationalityResponse
                .builder()
                .name(nationality.name())
                .code(nationality.getCode())
                .build();
    }
}

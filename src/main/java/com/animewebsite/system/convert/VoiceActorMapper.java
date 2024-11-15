package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.detail.VoiceActorDtoDetail;
import com.animewebsite.system.dto.res.lazy.VoiceActorDtoLazy;
import com.animewebsite.system.model.VoiceActor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {ImageMapper.class})
public interface VoiceActorMapper {

    @Mapping(source = "image",target = "image")
    VoiceActorDtoLazy voiceActorToVoiceActorDtoLazy(VoiceActor voiceActor);
}

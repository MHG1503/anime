package com.animewebsite.system.convert;

import com.animewebsite.system.dto.res.detail.AnimeDtoDetail;
import com.animewebsite.system.dto.res.detail.CharacterDtoDetail;
import com.animewebsite.system.dto.res.detail.VoiceActorDtoDetail;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeCharacterDtoLazy;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeCharacterVoiceActorDtoLazy;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeVoiceActorDtoLazy;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.CharacterVoiceActorDtoLazy;
import com.animewebsite.system.model.Anime;
import com.animewebsite.system.model.AnimeCharacterVoiceActor;
import com.animewebsite.system.model.Character;
import com.animewebsite.system.model.VoiceActor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {
        AnimeMapper.class,
        CharacterMapper.class,
        VoiceActorMapper.class,
        ImageMapper.class,
//        AlternativeTitleMapper.class,
//        GenreMapper.class,
//        ProducerMapper.class,
//        StudioMapper.class
})
public interface AnimeCharacterVoiceActorMapper {

    @Mapping(source = "animeCharacterVoiceActorId.anime",target = "animeDtoLazy")
    @Mapping(source = "animeCharacterVoiceActorId.character",target = "characterDtoLazy")
    @Mapping(source = "animeCharacterVoiceActorId.voiceActor",target = "voiceActorDtoLazy")
    AnimeCharacterVoiceActorDtoLazy animeCharacterVoiceActorToAnimeCharacterVoiceActorDtoLazy(AnimeCharacterVoiceActor animeCharacterVoiceActor);

    @Mapping(source = "animeCharacterVoiceActorId.anime",target = "animeDtoLazy")
    @Mapping(source = "animeCharacterVoiceActorId.character",target = "characterDtoLazy")
    AnimeCharacterDtoLazy animeCharacterVoiceActorToAnimeCharacterDtoLazy(AnimeCharacterVoiceActor animeCharacterVoiceActor);

    @Mapping(source = "animeCharacterVoiceActorId.anime",target = "animeDtoLazy")
    @Mapping(source = "animeCharacterVoiceActorId.voiceActor",target = "voiceActorDtoLazy")
    AnimeVoiceActorDtoLazy animeCharacterVoiceActorToAnimeVoiceActorDtoLazy(AnimeCharacterVoiceActor animeCharacterVoiceActor);

    @Mapping(source = "animeCharacterVoiceActorId.character",target = "characterDtoLazy")
    @Mapping(source = "animeCharacterVoiceActorId.voiceActor",target = "voiceActorDtoLazy")
    CharacterVoiceActorDtoLazy animeCharacterVoiceActorToCharacterVoiceActorDtoLazy(AnimeCharacterVoiceActor animeCharacterVoiceActor);

    @Mapping(source = "image",target = "image")
    @Mapping(source = "animeCharacterVoiceActors",target = "animeCharacterDtoLazies")
    VoiceActorDtoDetail voiceActorToVoiceActorDtoDetail(VoiceActor voiceActor);

    @Mapping(source = "image",target = "image")
    @Mapping(source = "animeCharacterVoiceActors",target = "animeVoiceActorDtoLazies")
    CharacterDtoDetail characterToCharacterDtoDetail(Character character);

}

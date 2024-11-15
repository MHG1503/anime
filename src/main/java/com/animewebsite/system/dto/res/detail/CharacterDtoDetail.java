package com.animewebsite.system.dto.res.detail;

import com.animewebsite.system.dto.res.lazy.ImageDtoLazy;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeVoiceActorDtoLazy;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CharacterDtoDetail {
    private Long id;

    private String name;

    private ImageDtoLazy image;

    private String about;

    private Set<AnimeVoiceActorDtoLazy> animeVoiceActorDtoLazies;
}

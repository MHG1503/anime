package com.animewebsite.system.dto.res.lazy.anime_character_voiceactor;

import com.animewebsite.system.dto.res.lazy.AnimeDtoLazy;
import com.animewebsite.system.dto.res.lazy.CharacterDtoLazy;
import com.animewebsite.system.dto.res.lazy.VoiceActorDtoLazy;
import com.animewebsite.system.model.enums.CharacterRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimeCharacterVoiceActorDtoLazy {
    private AnimeDtoLazy animeDtoLazy;
    private CharacterDtoLazy characterDtoLazy;
    private VoiceActorDtoLazy voiceActorDtoLazy;
    private CharacterRole characterRole;
}

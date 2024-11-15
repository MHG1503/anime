package com.animewebsite.system.dto.res.lazy.anime_character_voiceactor;

import com.animewebsite.system.dto.res.lazy.CharacterDtoLazy;
import com.animewebsite.system.dto.res.lazy.VoiceActorDtoLazy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterVoiceActorDtoLazy {
    private CharacterDtoLazy characterDtoLazy;
    private VoiceActorDtoLazy voiceActorDtoLazy;
}

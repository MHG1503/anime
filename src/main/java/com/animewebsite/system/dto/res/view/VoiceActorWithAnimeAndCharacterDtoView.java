package com.animewebsite.system.dto.res.view;

import com.animewebsite.system.dto.res.lazy.VoiceActorDtoLazy;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeCharacterDtoLazy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoiceActorWithAnimeAndCharacterDtoView {
    private VoiceActorDtoLazy voiceActor;
    private List<AnimeCharacterDtoLazy> animeCharacters;
}

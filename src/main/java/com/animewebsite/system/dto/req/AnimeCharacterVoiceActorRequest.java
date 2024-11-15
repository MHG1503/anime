package com.animewebsite.system.dto.req;

import com.animewebsite.system.model.enums.CharacterRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimeCharacterVoiceActorRequest{
    private Long animeId;
    private Long characterId;
    private Long voiceActorId;
    private CharacterRole characterRole;
}

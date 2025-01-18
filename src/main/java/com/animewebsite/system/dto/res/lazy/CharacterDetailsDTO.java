package com.animewebsite.system.dto.res.lazy;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CharacterDetailsDTO {
    private final Long characterId;
    private final String characterName;
    private final String about;
    private final ImageDtoLazy imageDtoLazy;
    private final List<Map<String, Object>> animeList;
    private final List<Map<String, Object>> voiceActorList;

    public CharacterDetailsDTO(Long characterId,
                               String characterName,
                               String about,
                               String imageUrl,
                               List<Map<String, Object>> animeList,
                               List<Map<String, Object>> voiceActorList) {
        this.characterId = characterId;
        this.characterName = characterName;
        this.about = about;
        this.imageDtoLazy = new ImageDtoLazy(imageUrl);
        this.animeList = animeList;
        this.voiceActorList = voiceActorList;
    }
}

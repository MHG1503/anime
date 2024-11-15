package com.animewebsite.system.id_convert;

import com.animewebsite.system.model.Anime;
import com.animewebsite.system.model.AnimeCharacterVoiceActor;
import com.animewebsite.system.model.Character;
import com.animewebsite.system.model.VoiceActor;
import com.animewebsite.system.model.pk.AnimeCharacterVoiceActorId;
import com.animewebsite.system.repository.AnimeRepository;
import com.animewebsite.system.repository.CharacterRepository;
import com.animewebsite.system.repository.VoiceActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class AnimeCharacterVoiceActorIdConverter implements BackendIdConverter {

    private final AnimeRepository animeRepository;

    private final CharacterRepository characterRepository;

    private final VoiceActorRepository voiceActorRepository;


    @Override
    public boolean supports(Class<?> type) {
        return AnimeCharacterVoiceActor.class.equals(type);
    }

    // Phần code chuyển đổi từ chuỗi ID sang đối tượng AnimeCharacterVoiceActorId
    @Override
    public Serializable fromRequestId(String id, Class<?> entityType) {
        // Tách chuỗi và chuyển thành AnimeCharacterVoiceActorId
        String[] parts = id.split("-");
        Long animeId = Long.parseLong(parts[0]);
        Long characterId = Long.parseLong(parts[1]);
        Long voiceActorId = Long.parseLong(parts[2]);

        Anime anime = animeRepository
                    .findById(animeId)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay anime voi id: " + animeId));

        Character character = characterRepository
                    .findById(characterId)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay nhan vat (character) voi id: " + characterId));

        VoiceActor voiceActor = voiceActorRepository
                    .findById(voiceActorId)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay nguoi long tieng (voice actor) voi id: " + voiceActorId));


        return new AnimeCharacterVoiceActorId(character,anime,voiceActor);
    }

    @Override
    public String toRequestId(Serializable id, Class<?> entityType) {
        AnimeCharacterVoiceActorId compositeId = (AnimeCharacterVoiceActorId) id;
        return compositeId.getAnime().getId() + "-" + compositeId.getCharacter().getId() + "-" + compositeId.getVoiceActor().getId();
    }
}
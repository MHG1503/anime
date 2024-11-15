package com.animewebsite.system.service;

import com.animewebsite.system.convert.AnimeCharacterVoiceActorMapper;
import com.animewebsite.system.dto.req.AnimeCharacterVoiceActorRequest;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeCharacterVoiceActorDtoLazy;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeVoiceActorDtoLazy;
import com.animewebsite.system.model.Anime;
import com.animewebsite.system.model.AnimeCharacterVoiceActor;
import com.animewebsite.system.model.Character;
import com.animewebsite.system.model.VoiceActor;
import com.animewebsite.system.model.enums.CharacterRole;
import com.animewebsite.system.model.pk.AnimeCharacterVoiceActorId;
import com.animewebsite.system.repository.AnimeCharacterVoiceActorRepository;
import com.animewebsite.system.repository.AnimeRepository;
import com.animewebsite.system.repository.CharacterRepository;
import com.animewebsite.system.repository.VoiceActorRepository;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AnimeCharacterVoiceActorService {
    private final AnimeCharacterVoiceActorRepository animeCharacterVoiceActorRepository;
    private final AnimeCharacterVoiceActorMapper animeCharacterVoiceActorMapper;
    private final AnimeRepository animeRepository;
    private final CharacterRepository characterRepository;
    private final VoiceActorRepository voiceActorRepository;


    @Transactional
    public AnimeCharacterVoiceActorDtoLazy linkRelationsAnimeCharacterVoiceActor(AnimeCharacterVoiceActorRequest animeCharacterVoiceActorRequest){

        Long animeId = animeCharacterVoiceActorRequest.getAnimeId();
        Long characterId = animeCharacterVoiceActorRequest.getCharacterId();
        Long voiceActorId = animeCharacterVoiceActorRequest.getVoiceActorId();
        CharacterRole characterRole = animeCharacterVoiceActorRequest.getCharacterRole();

        Anime anime =null;
        Character character = null;
        VoiceActor voiceActor = null;

        if(animeId != null) {
            anime = animeRepository
                    .findById(animeId)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay anime voi id: " + animeId));
        }

        if(characterId != null) {
            character = characterRepository
                    .findById(characterId)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay nhan vat (character) voi id: " + characterId));
        }

        if(voiceActorId != null) {
            voiceActor = voiceActorRepository
                    .findById(voiceActorId)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay nguoi long tieng (voice actor) voi id: " + voiceActorId));
        }
        
        AnimeCharacterVoiceActorId animeCharacterVoiceActorId = new AnimeCharacterVoiceActorId(character,anime,voiceActor);

        Optional<AnimeCharacterVoiceActor> animeCharacterVoiceActorOptional = animeCharacterVoiceActorRepository.findById(animeCharacterVoiceActorId);
        if(animeCharacterVoiceActorOptional.isPresent()){
            throw new RuntimeException("Da ton tai lien ket anime id: " +
                    animeId + " - character id: " +
                    characterId + " - voice actor id: " +
                    voiceActorId);
        }

        AnimeCharacterVoiceActor animeCharacterVoiceActor = AnimeCharacterVoiceActor
                .builder()
                .animeCharacterVoiceActorId(animeCharacterVoiceActorId)
                .characterRole(characterRole)
                .build();

        return animeCharacterVoiceActorMapper
                .animeCharacterVoiceActorToAnimeCharacterVoiceActorDtoLazy(animeCharacterVoiceActorRepository.save(animeCharacterVoiceActor));
    }

    @Transactional
    public AnimeCharacterVoiceActorDtoLazy updateLinkRelationsAnimeCharacterVoiceActor(AnimeCharacterVoiceActorId animeCharacterVoiceActorId,AnimeCharacterVoiceActorRequest animeCharacterVoiceActorRequest){
        Long animeId = animeCharacterVoiceActorRequest.getAnimeId();
        Long characterId = animeCharacterVoiceActorRequest.getCharacterId();
        Long voiceActorId = animeCharacterVoiceActorRequest.getVoiceActorId();

        Anime anime =null;
        Character character = null;
        VoiceActor voiceActor = null;

        if(animeId != null) {
            anime = animeRepository
                    .findById(animeId)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay anime voi id: " + animeId));
        }

        if(characterId != null) {
            character = characterRepository
                    .findById(characterId)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay nhan vat (character) voi id: " + characterId));
        }

        if(voiceActorId != null) {
            voiceActor = voiceActorRepository
                    .findById(voiceActorId)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay nguoi long tieng (voice actor) voi id: " + voiceActorId));
        }

        AnimeCharacterVoiceActor animeCharacterVoiceActor = animeCharacterVoiceActorRepository
                .findById(animeCharacterVoiceActorId)
                .orElseThrow(()->new RuntimeException("Khong tim thay lien ket nay"));

        var id = animeCharacterVoiceActor.getAnimeCharacterVoiceActorId();

        if(!Objects.equals(id.getAnime().getId(), animeId)){
            id.setAnime(anime);
        }
        if(!Objects.equals(id.getCharacter().getId(), characterId)) {
            id.setCharacter(character);
        }
        if(!Objects.equals(id.getVoiceActor().getId(), voiceActorId)) {
            id.setVoiceActor(voiceActor);
        }

        CharacterRole characterRole = animeCharacterVoiceActorRequest.getCharacterRole();
        animeCharacterVoiceActor.setCharacterRole(characterRole);

        return animeCharacterVoiceActorMapper
                .animeCharacterVoiceActorToAnimeCharacterVoiceActorDtoLazy(animeCharacterVoiceActorRepository.save(animeCharacterVoiceActor));
    }

    @Transactional
    public void deleteLink(AnimeCharacterVoiceActorId animeCharacterVoiceActorId){
        AnimeCharacterVoiceActor animeCharacterVoiceActor = animeCharacterVoiceActorRepository
                .findById(animeCharacterVoiceActorId)
                .orElseThrow(()->new RuntimeException("Khong tim thay lien ket nay"));
        animeCharacterVoiceActorRepository.delete(animeCharacterVoiceActor);
    }
}

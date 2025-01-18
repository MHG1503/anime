package com.animewebsite.system.service;

import com.animewebsite.system.convert.AnimeCharacterVoiceActorMapper;
import com.animewebsite.system.dto.req.AnimeCharacterVoiceActorRequest;
import com.animewebsite.system.dto.req.ChangeCharacterRoleRequest;
import com.animewebsite.system.dto.res.NationalityResponse;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.dto.res.lazy.CharacterDetailsDTO;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeCharacterVoiceActorDtoLazy;

import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.CharacterVoiceActorDtoLazy;
import com.animewebsite.system.dto.res.view.CharacterVoiceActorRoleViewDto;
import com.animewebsite.system.dto.res.view.VoiceActorViewDto;
import com.animewebsite.system.model.Anime;
import com.animewebsite.system.model.AnimeCharacterVoiceActor;
import com.animewebsite.system.model.Character;
import com.animewebsite.system.model.VoiceActor;
import com.animewebsite.system.model.enums.CharacterRole;
import com.animewebsite.system.model.enums.Nationality;
import com.animewebsite.system.model.pk.AnimeCharacterVoiceActorId;
import com.animewebsite.system.repository.AnimeCharacterVoiceActorRepository;
import com.animewebsite.system.repository.AnimeRepository;
import com.animewebsite.system.repository.CharacterRepository;
import com.animewebsite.system.repository.VoiceActorRepository;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class AnimeCharacterVoiceActorService {
    private final AnimeCharacterVoiceActorRepository animeCharacterVoiceActorRepository;
    private final AnimeCharacterVoiceActorMapper animeCharacterVoiceActorMapper;
    private final AnimeRepository animeRepository;
    private final CharacterRepository characterRepository;
    private final VoiceActorRepository voiceActorRepository;
    private final ObjectMapper objectMapper;


    public PaginatedResponse<?> getAllCharacterWithTheirVoiceActorByAnimeId(Long id, int pageNum, int pageSize){
        var result = new ArrayList<>();
        var data = animeCharacterVoiceActorRepository.getAllCharacterWithTheirVoiceActorByAnimeId(id,PageRequest.of(pageNum - 1,pageSize));
        for (Object item : data.getContent()) {
            Object[] row = (Object[]) item;

            Long characterId = ((Number) row[0]).longValue();
            String characterName = (String) row[1];
            String characterImage = (String) row[2];
            String role = (String) row[3];

            String json = (String) row[4];
            List<VoiceActorViewDto> voiceActors = new ArrayList<>();

            try {
                voiceActors = objectMapper.readValue(json, new TypeReference<List<VoiceActorViewDto>>() {});

                for (VoiceActorViewDto voiceActor : voiceActors) {
                    String nationalityStr =(String) voiceActor.getNationality();

                    try {
                        Nationality nationalityEnum = Nationality.valueOf(nationalityStr);
                        NationalityResponse nationalityResponse = NationalityResponse
                                .builder()
                                .name(nationalityEnum.name())
                                .code(nationalityEnum.getCode())
                                .build();
                        voiceActor.setNationality(nationalityResponse);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Lỗi: Quốc tịch không hợp lệ - " + nationalityStr);
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            result.add(new CharacterVoiceActorRoleViewDto(characterId, characterName,characterImage, role, voiceActors));
        }
        return new PaginatedResponse<Object>(result, data.getTotalPages(), data.getNumber() + 1 , data.getTotalElements());
    }

    public PaginatedResponse<?> getAllRelationshipByOption(Long id,String option, int pageNum, int pageSize){
        return switch (option){
            case "anime" -> {
                Pageable pageable = PageRequest.of(pageNum - 1, pageSize).withSort(Sort.by("animeCharacterVoiceActorId.character.name"));

                var result = animeCharacterVoiceActorRepository
                        .findByAnimeCharacterVoiceActorIdAnimeId(id,NamedEntityGraph.fetching("anime-character-voice_actor"),pageable);
                yield new PaginatedResponse<>(
                        result.stream().map(animeCharacterVoiceActorMapper::animeCharacterVoiceActorToCharacterVoiceActorDtoLazy).toList(),
                        result.getTotalPages(),
                        result.getNumber() + 1,
                        result.getTotalElements()
                );
            }

            case "character" -> {
                Pageable pageable = PageRequest.of(pageNum - 1, pageSize).withSort(Sort.by("animeCharacterVoiceActorId.anime.name"));

                var result = animeCharacterVoiceActorRepository
                        .findByAnimeCharacterVoiceActorIdCharacterId(id,NamedEntityGraph.fetching("anime-character-voice_actor"),pageable);
                yield new PaginatedResponse<>(
                        result.stream().map(animeCharacterVoiceActorMapper::animeCharacterVoiceActorToAnimeVoiceActorDtoLazy).toList(),
                        result.getTotalPages(),
                        result.getNumber() + 1,
                        result.getTotalElements()
                );
            }

            case "voice_actor" -> {
                Pageable pageable = PageRequest.of(pageNum - 1, pageSize).withSort(Sort.by("animeCharacterVoiceActorId.anime.name"));

                var result = animeCharacterVoiceActorRepository
                        .findByAnimeCharacterVoiceActorIdVoiceActorId(id,NamedEntityGraph.fetching("anime-character-voice_actor"),pageable);
                yield new PaginatedResponse<>(
                        result.stream().map(animeCharacterVoiceActorMapper::animeCharacterVoiceActorToAnimeCharacterDtoLazy).toList(),
                        result.getTotalPages(),
                        result.getNumber() + 1,
                        result.getTotalElements()
                );
            }

            default -> null;
        };
    }

    @Transactional
    public CharacterVoiceActorDtoLazy linkRelationsAnimeCharacterVoiceActor(AnimeCharacterVoiceActorRequest animeCharacterVoiceActorRequest){

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

        boolean isAnimeCharacterVoiceActor = animeCharacterVoiceActorRepository
                .existsByAnimeIdCharacterIdAndCharacterRole(
                        animeId,
                        characterId,
                        !animeCharacterVoiceActorRequest.getCharacterRole().equals(CharacterRole.MAIN) ? CharacterRole.MAIN : CharacterRole.SUPPORTING);
        if(isAnimeCharacterVoiceActor){
            throw new RuntimeException("Khong the them character voi nhieu role trong 1 bo anime !!!");
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
                .animeCharacterVoiceActorToCharacterVoiceActorDtoLazy(animeCharacterVoiceActorRepository.save(animeCharacterVoiceActor));
    }

    @Transactional
    public AnimeCharacterVoiceActorDtoLazy updateLinkRelationsAnimeCharacterVoiceActor(AnimeCharacterVoiceActorId originId,AnimeCharacterVoiceActorRequest animeCharacterVoiceActorRequest){
        Long animeIdReq = animeCharacterVoiceActorRequest.getAnimeId();
        Long characterIdReq = animeCharacterVoiceActorRequest.getCharacterId();
        Long voiceActorIdReq = animeCharacterVoiceActorRequest.getVoiceActorId();
        Long originCharacterId = originId.getCharacter().getId();
        Long originVoiceActorId = originId.getVoiceActor().getId();

        Character character = null;
        VoiceActor voiceActor = null;

        if(animeIdReq != null) {
            animeRepository
                    .findById(animeIdReq)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay anime voi id: " + animeIdReq));
        }

        if(characterIdReq != null) {
            character = characterRepository
                    .findById(characterIdReq)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay nhan vat (character) voi id: " + characterIdReq));
        }

        if(voiceActorIdReq != null) {
            voiceActor = voiceActorRepository
                    .findById(voiceActorIdReq)
                    .orElseThrow(() -> new RuntimeException("Khong tim thay nguoi long tieng (voice actor) voi id: " + voiceActorIdReq));
        }

        boolean isAnimeCharacterVoiceActor = animeCharacterVoiceActorRepository
                .existsByAnimeIdCharacterIdAndCharacterRole(
                        animeIdReq,
                        characterIdReq,
                        !animeCharacterVoiceActorRequest.getCharacterRole().equals(CharacterRole.MAIN) ? CharacterRole.MAIN : CharacterRole.SUPPORTING);
        
//        if(isAnimeCharacterVoiceActor){
//            throw new RuntimeException("Khong the them character voi nhieu role trong 1 bo anime !!!");
//        }

        AnimeCharacterVoiceActor animeCharacterVoiceActor = animeCharacterVoiceActorRepository
                .findById(originId)
                .orElseThrow(()->new RuntimeException("Khong tim thay lien ket nay"));

        AnimeCharacterVoiceActorId id = animeCharacterVoiceActor.getAnimeCharacterVoiceActorId();

        if(!Objects.equals(id.getCharacter().getId(), characterIdReq)) {
            id.setCharacter(character);
        }
        if(!Objects.equals(id.getVoiceActor().getId(), voiceActorIdReq)) {
            id.setVoiceActor(voiceActor);
        }

        Optional<AnimeCharacterVoiceActor> animeCharacterVoiceActorOptional = animeCharacterVoiceActorRepository.findById(id);
        if(animeCharacterVoiceActorOptional.isPresent()){
            throw new RuntimeException("Da ton tai lien ket anime id: " +
                    animeIdReq + " - character id: " +
                    characterIdReq + " - voice actor id: " +
                    voiceActorIdReq);
        }

        CharacterRole characterRole = animeCharacterVoiceActorRequest.getCharacterRole();
        animeCharacterVoiceActor.setCharacterRole(characterRole);

        animeCharacterVoiceActorRepository.updateRelation(
                originId.getAnime().getId(),
                originCharacterId,
                originVoiceActorId,
                characterIdReq,
                voiceActorIdReq
                );
        return animeCharacterVoiceActorMapper
                .animeCharacterVoiceActorToAnimeCharacterVoiceActorDtoLazy(animeCharacterVoiceActorRepository
                        .findById(id)
                        .orElseThrow(()-> new RuntimeException("Update that bai!")));
    }

    @Transactional
    public void updateAllCharacterRoleOfCharacterInAnime(ChangeCharacterRoleRequest request){
        animeCharacterVoiceActorRepository
                .updateCharacterRoleByCharacterIdAndAnimeId(request.getAnimeId(), request.getCharacterId(), CharacterRole.valueOf(request.getCharacterRole()));
    }

    @Transactional
    public void deleteLink(AnimeCharacterVoiceActorId animeCharacterVoiceActorId){
        AnimeCharacterVoiceActor animeCharacterVoiceActor = animeCharacterVoiceActorRepository
                .findById(animeCharacterVoiceActorId)
                .orElseThrow(()->new RuntimeException("Khong tim thay lien ket nay"));
        animeCharacterVoiceActorRepository.delete(animeCharacterVoiceActor);
    }
}

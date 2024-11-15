package com.animewebsite.system.controller;

import com.animewebsite.system.dto.req.AnimeCharacterVoiceActorRequest;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeCharacterVoiceActorDtoLazy;
import com.animewebsite.system.id_convert.AnimeCharacterVoiceActorIdConverter;
import com.animewebsite.system.model.AnimeCharacterVoiceActor;
import com.animewebsite.system.model.pk.AnimeCharacterVoiceActorId;
import com.animewebsite.system.service.AnimeCharacterVoiceActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/animes/characters/voice-actors")
@RequiredArgsConstructor
public class AnimeCharacterVoiceActorAssociationController {

    private final AnimeCharacterVoiceActorService animeCharacterVoiceActorService;
    private final AnimeCharacterVoiceActorIdConverter animeCharacterVoiceActorIdConverter;

    @PostMapping("/create/link")
    public ResponseEntity<?> handleLinkAssociations(@RequestBody AnimeCharacterVoiceActorRequest animeCharacterVoiceActorRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(animeCharacterVoiceActorService.linkRelationsAnimeCharacterVoiceActor(animeCharacterVoiceActorRequest));
    }

    @PostMapping("/update/link/{id}")
    public ResponseEntity<?> handleUnLinkAssociations(@PathVariable("id") String id, @RequestBody AnimeCharacterVoiceActorRequest animeCharacterVoiceActorRequest){
        AnimeCharacterVoiceActorId animeCharacterVoiceActorId = (AnimeCharacterVoiceActorId) animeCharacterVoiceActorIdConverter.fromRequestId(id, AnimeCharacterVoiceActor.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(animeCharacterVoiceActorService.updateLinkRelationsAnimeCharacterVoiceActor(animeCharacterVoiceActorId,animeCharacterVoiceActorRequest));
    }

    @DeleteMapping("/delete/link/{id}")
    public ResponseEntity<?> deleteLinkAssociations(@PathVariable("id") String id){
        AnimeCharacterVoiceActorId animeCharacterVoiceActorId = (AnimeCharacterVoiceActorId) animeCharacterVoiceActorIdConverter.fromRequestId(id, AnimeCharacterVoiceActor.class);
        animeCharacterVoiceActorService.deleteLink(animeCharacterVoiceActorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Delete link thanh cong");
    }
}

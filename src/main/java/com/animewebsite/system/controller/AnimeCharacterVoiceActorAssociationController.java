package com.animewebsite.system.controller;

import com.animewebsite.system.dto.DataResponse;
import com.animewebsite.system.dto.req.AnimeCharacterVoiceActorRequest;
import com.animewebsite.system.dto.req.ChangeCharacterRoleRequest;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeCharacterVoiceActorDtoLazy;
import com.animewebsite.system.id_convert.AnimeCharacterVoiceActorIdConverter;
import com.animewebsite.system.model.AnimeCharacterVoiceActor;
import com.animewebsite.system.model.pk.AnimeCharacterVoiceActorId;
import com.animewebsite.system.service.AnimeCharacterVoiceActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/animes/characters/voice-actors")
@RequiredArgsConstructor
public class AnimeCharacterVoiceActorAssociationController {

    private final AnimeCharacterVoiceActorService animeCharacterVoiceActorService;
    private final AnimeCharacterVoiceActorIdConverter animeCharacterVoiceActorIdConverter;

    @GetMapping("/{option}/{id}")
    public ResponseEntity<?> getAllRelationshipByOption(@PathVariable("id") Long id ,
                                                        @PathVariable("option") String option,
                                                        @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize){
        var result = animeCharacterVoiceActorService.getAllRelationshipByOption(id,option,pageNum,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                result
                        )
                );
    }

    @GetMapping("/c-and-va/anime_id={id}")
    public ResponseEntity<?> getAllCharacterWithTheirVoiceActorByAnimeId(@PathVariable("id") Long id ,
                                                        @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize){
        var result = animeCharacterVoiceActorService.getAllCharacterWithTheirVoiceActorByAnimeId(id,pageNum,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                result
                        )
                );
    }
    
    @PostMapping("/create/link")
    public ResponseEntity<?> handleLinkAssociations(@RequestBody AnimeCharacterVoiceActorRequest animeCharacterVoiceActorRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new DataResponse(
                                HttpStatus.CREATED.value(),
                                animeCharacterVoiceActorService.linkRelationsAnimeCharacterVoiceActor(animeCharacterVoiceActorRequest)
                        )
                );
    }

    @PutMapping("/update/link/{id}")
    public ResponseEntity<?> handleUnLinkAssociations(@PathVariable("id") String id, @RequestBody AnimeCharacterVoiceActorRequest animeCharacterVoiceActorRequest){
        AnimeCharacterVoiceActorId animeCharacterVoiceActorId = (AnimeCharacterVoiceActorId) animeCharacterVoiceActorIdConverter.fromRequestId(id, AnimeCharacterVoiceActor.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                animeCharacterVoiceActorService.updateLinkRelationsAnimeCharacterVoiceActor(animeCharacterVoiceActorId,animeCharacterVoiceActorRequest)
                        )
                );
    }

    @PostMapping("/update/link/character_role")
    public ResponseEntity<?> changeCharacterRoleOfCharacterInAnime(@RequestBody ChangeCharacterRoleRequest request){
        animeCharacterVoiceActorService.updateAllCharacterRoleOfCharacterInAnime(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "Thay doi thanh cong"
                        )
                );
    }

    @DeleteMapping("/delete/link/{id}")
    public ResponseEntity<?> deleteLinkAssociations(@PathVariable("id") String id){
        AnimeCharacterVoiceActorId animeCharacterVoiceActorId = (AnimeCharacterVoiceActorId) animeCharacterVoiceActorIdConverter.fromRequestId(id, AnimeCharacterVoiceActor.class);
        animeCharacterVoiceActorService.deleteLink(animeCharacterVoiceActorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "Delete link thanh cong"
                        )
                );
    }
}

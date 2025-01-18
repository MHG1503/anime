package com.animewebsite.system.controller;

import com.animewebsite.system.dto.DataResponse;
import com.animewebsite.system.dto.req.CharacterRequest;
import com.animewebsite.system.service.CharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;

@RestController
@RequestMapping("api/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;

    @GetMapping("/full-info/{id}")
    public ResponseEntity<?> findCharacterWithAnimeAndVoiceActors(@PathVariable("id") Long id ){
        var result = characterService.getCharacterDetails(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                result
                        )
                );
    }

    @GetMapping
    public ResponseEntity<?> getAllCharacters(@RequestParam(value = "pageNum",defaultValue = "1",required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "15",required = false) Integer pageSize){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                characterService.getAllCharacters(pageNum,pageSize)
                        )
                );
    }

    @GetMapping("/selections")
    public ResponseEntity<?> getSelectionCharacter(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                characterService.getAllSelectionCharacters()
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacterById(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                characterService.getCharacterById(id)
                        )
                );
    }

    @PostMapping(value = "/create",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCharacter(@RequestPart("characterRequest") @Valid CharacterRequest characterRequest,
                                             @RequestPart(value = "image",required = false) MultipartFile multipartFile){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.CREATED.value(),
                                characterService.createCharacter(characterRequest,multipartFile)
                        )
                );
    }

    @PutMapping(value = "/update/{id}",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateCharacter(@PathVariable("id") Long id,
                                             @RequestPart("characterRequest") @Valid CharacterRequest characterRequest,
                                             @RequestPart(value = "image",required = false) MultipartFile multipartFile){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                characterService.updateCharacter(id,characterRequest,multipartFile)
                        )
                );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable("id") Long id){
        characterService.deleteCharacterById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "Xoa thanh cong character"
                        )
                );
    }
}

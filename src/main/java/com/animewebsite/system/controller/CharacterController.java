package com.animewebsite.system.controller;

import com.animewebsite.system.dto.req.CharacterRequest;
import com.animewebsite.system.service.CharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;

@RestController
@RequestMapping("api/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;

    @GetMapping
    public ResponseEntity<?> getAllCharacters(@RequestParam(value = "pageNum",defaultValue = "1",required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "15",required = false) Integer pageSize){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(characterService.getAllCharacters(pageNum,pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacterById(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(characterService.getCharacterById(id));
    }

    @PostMapping(value = "/create",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCharacter(@RequestPart("characterRequest") @Valid CharacterRequest characterRequest,
                                             @RequestPart(value = "image",required = false) MultipartFile multipartFile){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(characterService.createCharacter(characterRequest,multipartFile));
    }

    @PutMapping(value = "/update/{id}",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateCharacter(@PathVariable("id") Long id,
                                             @RequestPart("characterRequest") @Valid CharacterRequest characterRequest,
                                             @RequestPart(value = "image",required = false) MultipartFile multipartFile){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(characterService.updateCharacter(id,characterRequest,multipartFile));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(characterService.deleteCharacterById(id));
    }
}

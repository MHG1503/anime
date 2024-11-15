package com.animewebsite.system.controller;

import com.animewebsite.system.dto.req.VoiceActorRequest;
import com.animewebsite.system.service.VoiceActorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/voice_actors")
@RequiredArgsConstructor
public class VoiceActorController {
    private final VoiceActorService voiceActorService;

    @GetMapping
    public ResponseEntity<?> getAllVoiceActors(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(voiceActorService.getAllVoiceActors(pageNum,pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVoiceActorById(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(voiceActorService.getVoiceActorById(id));
    }

    @PostMapping(value = "/create",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createVoiceActor(@RequestPart("voiceActorRequest") @Valid VoiceActorRequest voiceActorRequest,
                                              @RequestPart(value = "image",required = false)MultipartFile multipartFile){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(voiceActorService.createVoiceActor(voiceActorRequest,multipartFile));
    }

    @PutMapping(value = "/update/{id}",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateVoiceActor(@PathVariable("id") Long id,
                                              @RequestPart("voiceActorRequest") @Valid VoiceActorRequest voiceActorRequest,
                                              @RequestPart(value = "image",required = false)MultipartFile multipartFile){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(voiceActorService.updateVoiceActor(id,voiceActorRequest,multipartFile));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVoiceActor(@PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(voiceActorService.deleteVoiceActor(id));
    }
}

package com.animewebsite.system.controller;

import com.animewebsite.system.dto.DataResponse;
import com.animewebsite.system.dto.req.EpisodeRequest;
import com.animewebsite.system.dto.req.PromotionVideoRequest;
import com.animewebsite.system.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/episodes")
public class EpisodeController {
    private final EpisodeService episodeService;

    @PostMapping(path = "/create",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addEpisode(@RequestPart("request") EpisodeRequest request,
                                        @RequestPart(name = "file",required = false)MultipartFile file,
                                        @RequestPart(name = "image",required = false)MultipartFile imageFile){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new DataResponse(
                                HttpStatus.CREATED.value(),
                                episodeService.createAndAddEpisode(request,file,imageFile)
                        )
                );
    }

    @PutMapping(path = "/update/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateEpisode(@RequestPart("request") EpisodeRequest request,
                                        @RequestPart(name = "file",required = false)MultipartFile file,
                                        @RequestPart(name = "image",required = false)MultipartFile imageFile,
                                           @PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                episodeService.updateEpisode(id,request,file,imageFile)
                        )
                );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEpisode(@PathVariable("id") Long id,@RequestParam("animeId")Long animeId){
        episodeService.deleteEpisode(id,animeId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "Xoa thanh cong Episode"
                        )
                );
    }
}

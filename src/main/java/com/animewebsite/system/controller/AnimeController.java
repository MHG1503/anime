package com.animewebsite.system.controller;

import com.animewebsite.system.dto.DataResponse;
import com.animewebsite.system.dto.req.CreateAnimeRequest;
import com.animewebsite.system.dto.req.UpdateAnimeRequest;
import com.animewebsite.system.model.Anime;
import com.animewebsite.system.service.AnimeService;
import com.animewebsite.system.service.CloudinaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/animes")
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeService animeService;


    @GetMapping
    public ResponseEntity<?> getAllAnime(@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                animeService.getAllAnime(pageNum,pageSize)
                        )
                );
    }

    @GetMapping("/series/{id}")
    public ResponseEntity<?> getAllAnimeBySeriesId(@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                                                   @PathVariable("id") String id
                                                   ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                animeService.getAllAnimeBySeriesId(pageNum,pageSize, Objects.equals(id, "none") ? null : Long.parseLong(id))
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnimeById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                animeService.getAnimeById(id)
                        )
                );
    }

    @PostMapping(value = "/create",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAnime(@RequestPart(name = "image",required = false)MultipartFile multipartFile,
                                         @RequestPart("animeData") @Valid CreateAnimeRequest createAnimeRequest) throws IOException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new DataResponse(
                                HttpStatus.CREATED.value(),
                                animeService.createAnime(createAnimeRequest,multipartFile)
                        )
                );
    }

    @PutMapping(value = "/update/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAnime(@PathVariable("id") Long id,
                                         @RequestPart(value = "image",required = false)MultipartFile file,
                                         @RequestPart("animeData") @Valid UpdateAnimeRequest updateAnimeRequest) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                animeService.updateAnime(id,updateAnimeRequest,file)
                        )
                );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnime(@PathVariable("id") Long id){
        animeService.deleteAnimeById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "Xoa thanh cong anime"
                        )
                );
    }
}

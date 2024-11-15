package com.animewebsite.system.controller;

import com.animewebsite.system.dto.DataResponse;
import com.animewebsite.system.model.Trailer;
import com.animewebsite.system.service.TrailerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/trailers")
public class TrailerController {
    private final TrailerService trailerService;

    @PostMapping("/create")
    public ResponseEntity<?> createTrailer(@RequestBody Trailer trailer){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                trailerService.createTrailer(trailer)
                        )
                );
    }
}

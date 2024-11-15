package com.animewebsite.system.controller;

import com.animewebsite.system.dto.req.GenreRequest;
import com.animewebsite.system.model.Genre;
import com.animewebsite.system.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("api/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<?> getAllGenres(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreService.getGenreById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewGenre(@RequestBody GenreRequest genreRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreService.createGenre(genreRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable("id")Long id,@RequestBody GenreRequest genreRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreService.updateGenre(id,genreRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable("id")Long id){
        Genre genre = genreService.deleteGenre(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Xoa thanh cong genre id " + genre.getId() + " : " + genre.getName());
    }
}

package com.animewebsite.system.controller;

import com.animewebsite.system.dto.req.SeriesRequest;
import com.animewebsite.system.dto.res.detail.SeriesDtoDetail;
import com.animewebsite.system.service.SeriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/series")
@RequiredArgsConstructor
public class SeriesController {
    private final SeriesService seriesService;

    @GetMapping
    public ResponseEntity<?> getAllSeries(@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesService.getAllSeries(pageNum,pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSeriesById(@PathVariable("id")Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSeries(@RequestBody @Valid SeriesRequest seriesRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesService.createSeries(seriesRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSeriesInfo(@PathVariable("id") Long id,@RequestBody @Valid SeriesRequest seriesRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seriesService.updateSeries(id,seriesRequest));
    }

    @PostMapping("/{seriesId}/anime/{animeId}")
    public ResponseEntity<?> manageAnimeInSeries(
            @PathVariable Long seriesId,
            @PathVariable Long animeId,
            @RequestParam boolean isAdd) {

        SeriesDtoDetail seriesDtoDetail = seriesService.manageAnimeInSeries(seriesId, animeId, isAdd);
        return ResponseEntity.ok(seriesDtoDetail);
    }
}

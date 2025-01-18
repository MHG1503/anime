package com.animewebsite.system.controller;

import com.animewebsite.system.dto.DataResponse;
import com.animewebsite.system.dto.req.EpisodeRequest;
import com.animewebsite.system.dto.req.PromotionVideoRequest;
import com.animewebsite.system.service.PromotionVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/promotion_videos")
public class PromotionVideoController {
    private final PromotionVideoService promotionVideoService;

    @GetMapping
    public ResponseEntity<?> getPromotionVideosByAnimeId(@RequestParam("pageNum") Integer pageNum,
                                                         @RequestParam("pageSize") Integer pageSize,
                                                         @RequestParam("animeId") Long animeId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                promotionVideoService.getAllPromotionVideosByAnimeId(animeId,pageNum,pageSize)
                        )
                );
    }


    @PostMapping("/create")
    public ResponseEntity<?> addPromotionVideo(@RequestBody PromotionVideoRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new DataResponse(
                            HttpStatus.CREATED.value(),
                            promotionVideoService.createAndAddPromotionVideo(request)
                        )
                );
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePromotionVideo(@PathVariable("id") Long id,
                                                  @RequestBody PromotionVideoRequest request){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                promotionVideoService.updatePromotionVideo(id,request)
                        )
                );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePromotionVideo(@PathVariable("id") Long id){
        promotionVideoService.deletePromotionVideo(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                               "Xoa thanh cong promotion video!"
                        )
                );
    }
}

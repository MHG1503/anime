package com.animewebsite.system.controller;

import com.animewebsite.system.dto.req.StudioRequest;
import com.animewebsite.system.model.Studio;
import com.animewebsite.system.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/studios")
@RequiredArgsConstructor
public class StudioController {
    private final StudioService studioService;

    @GetMapping
    public ResponseEntity<?> getAllStudios(@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studioService.getAllStudios(pageNum,pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudioById(@PathVariable("id")Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studioService.getStudioById(id));
    }

    @PostMapping(value = "/create" ,consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createStudio(@RequestPart(value = "studioRequest") StudioRequest studioRequest,
                                            @RequestPart(value = "image",required = false) MultipartFile multipartFile){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studioService.createStudio(studioRequest,multipartFile));
    }

    @PutMapping(value = "/update/{id}",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateStudio(@PathVariable("id") Long id,
                                            @RequestPart(value = "studioRequest",required = true)StudioRequest studioRequest,
                                            @RequestPart(value = "image",required = false) MultipartFile multipartFile){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studioService.updateStudio(id,studioRequest,multipartFile));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudio(@PathVariable("id") Long id){
        studioService.deleteStudio(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Xoa studio thanh cong");
    }
}

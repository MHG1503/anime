package com.animewebsite.system.controller;

import com.animewebsite.system.dto.DataResponse;
import com.animewebsite.system.dto.req.StudioRequest;
import com.animewebsite.system.model.Studio;
import com.animewebsite.system.service.StudioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                studioService.getAllStudios(pageNum,pageSize)
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudioById(@PathVariable("id")Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                studioService.getStudioById(id)
                        )
                );
    }

    @PostMapping(value = "/create" ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createStudio(@Valid StudioRequest studioRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                studioService.createStudio(studioRequest)
                        )
                );
    }

    @PutMapping(value = "/update/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateStudio(@PathVariable("id") Long id,
                                            @Valid StudioRequest studioRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                studioService.updateStudio(id,studioRequest)
                        )
                );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudio(@PathVariable("id") Long id){
        studioService.deleteStudio(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "Xoa studio thanh cong"
                        )
                );
    }
}

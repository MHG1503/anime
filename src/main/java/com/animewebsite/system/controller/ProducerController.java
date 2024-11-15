package com.animewebsite.system.controller;

import com.animewebsite.system.dto.req.ProducerRequest;
import com.animewebsite.system.model.Producer;
import com.animewebsite.system.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/producers")
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerService producerService;

    @GetMapping
    public ResponseEntity<?> getAllProducer(@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(producerService.getAllProducers(pageNum,pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProducerById(@PathVariable("id")Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(producerService.getProducerById(id));
    }

    @PostMapping(value = "/create" ,consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createProducer(@RequestPart(value = "producerRequest")ProducerRequest producerRequest,
                                            @RequestPart(value = "image",required = false) MultipartFile multipartFile){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(producerService.createProducer(producerRequest,multipartFile));
    }

    @PutMapping(value = "update/{id}",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateProducer(@PathVariable("id") Long id,
                                            @RequestPart(value = "producerRequest",required = true)ProducerRequest producerRequest,
                                            @RequestPart(value = "image",required = false) MultipartFile multipartFile){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(producerService.updateProducer(id,producerRequest,multipartFile));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudio(@PathVariable("id") Long id){
        producerService.deleteProducer(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Xoa producer thanh cong");
    }
}

package com.animewebsite.system.controller;

import com.animewebsite.system.dto.DataResponse;
import com.animewebsite.system.dto.req.ProducerRequest;
import com.animewebsite.system.service.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                producerService.getAllProducers(pageNum,pageSize)
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProducerById(@PathVariable("id")Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                producerService.getProducerById(id)
                        )
                );
    }

    @PostMapping(value = "/create" ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createProducer(@Valid ProducerRequest producerRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                producerService.createProducer(producerRequest)
                        )
                );
    }

    @PutMapping(value = "update/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateProducer(@PathVariable("id") Long id,
                                            @Valid ProducerRequest producerRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                producerService.updateProducer(id,producerRequest)
                        )
                );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudio(@PathVariable("id") Long id){
        producerService.deleteProducer(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "Xoa producer thanh cong"
                        )
                );
    }
}

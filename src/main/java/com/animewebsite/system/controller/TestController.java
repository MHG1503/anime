package com.animewebsite.system.controller;

import com.animewebsite.system.service.TestFileService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {
    private final TestFileService testFileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("file")MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String result = testFileService.upload(file);
        return ResponseEntity.ok(result);
    }
}

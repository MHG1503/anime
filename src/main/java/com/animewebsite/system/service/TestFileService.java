package com.animewebsite.system.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TestFileService {
    private final MinioClient minioClient;

    public String upload(@NonNull MultipartFile multipartFile) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try{
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket("test")
                            .object(multipartFile.getOriginalFilename())
                            .contentType(Objects.isNull(multipartFile.getContentType()) ? "image/png; image/jpg;" : multipartFile.getContentType())
                            .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                            .build()
            );
        }catch (Exception e){
            e.printStackTrace();
        }

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                        .builder()
                        .method(Method.GET)
                        .bucket("test")
                        .object(multipartFile.getOriginalFilename())
                        .build()
        );
    }
}

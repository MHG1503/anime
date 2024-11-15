package com.animewebsite.system.integration;

import io.minio.*;
import io.minio.messages.Item;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioChannel {
    private final MinioClient minioClient;
    @SneakyThrows
    private void createBucket(final String name){
        final var found = minioClient.bucketExists(
                BucketExistsArgs
                        .builder()
                        .bucket(name)
                        .build()
        );

        if(!found){
            minioClient.makeBucket(
                    MakeBucketArgs
                            .builder()
                            .bucket(name)
                            .build()
            );

            final var policy = """
                        {
                          "Version": "2012-10-17",
                          "Statement": [
                           {
                              "Effect": "Allow",
                              "Principal": "*",
                              "Action": "s3:GetObject",
                              "Resource": "arn:aws:s3:::%s/*"
                            }
                          ]
                        }
                    """.formatted(name);
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs
                            .builder()
                            .bucket(name)
                            .config(policy)
                            .build()
            );
        }else {
            log.info("Bucket %s đã tồn tại.".formatted(name));
        }
    }

    @SneakyThrows
    public void delete(String bucket,String folder){

        Iterable<Result<Item>> items = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(folder)
                        .recursive(true)
                        .build()
        );

        List<String> objectsToDelete = new ArrayList<>();
        for (Result<Item> item : items) {
            objectsToDelete.add(item.get().objectName());
        }

        for (String objectName : objectsToDelete) {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
            log.info("Deleted file: {} from bucket: {}", objectName, bucket);

        }
    }

    @SneakyThrows
    public String upload(@NotBlank final String bucket, @NonNull final String folder,@NonNull final MultipartFile file){

        createBucket(bucket);
        log.info("Bucket: {}, name: {} ,file size: {}", bucket, file.getOriginalFilename() ,file.getSize());

        final var fileName = file.getOriginalFilename();
        String objectName = folder + "/" + fileName;

        if (doesFileExist(bucket, objectName)) {
            log.warn("File already exists: {}", objectName);
            throw new RuntimeException("File already exists in the folder: " + objectName);
        }

        try {
             minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .contentType(Objects.isNull(file.getContentType()) ? "image/png; image/jpg;" : file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );
        } catch (Exception ex) {
            log.error("Error saving file \n {} ", ex.getMessage());
            throw new RuntimeException("Khong the upload file");
        }
        return minioClient.getPresignedObjectUrl(
                io.minio.GetPresignedObjectUrlArgs.builder()
                        .method(io.minio.http.Method.GET)
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );
    }

    @SneakyThrows
    public boolean doesFileExist(@NotBlank String bucket, @NotBlank String objectName) {
        try {
            // Kiểm tra xem đối tượng (tệp) có tồn tại hay không
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
            return true; // Tệp tồn tại
        } catch (io.minio.errors.ErrorResponseException ex) {
            if ("NoSuchKey".equals(ex.errorResponse().code())) {
                return false; // Tệp không tồn tại
            }
            // Nếu lỗi không phải "NoSuchKey", ném lỗi để xử lý
            throw ex;
        }
    }

    public void copyAndDelete(String bucketName, String tempFolder, String finalFolder) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).prefix(tempFolder).build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                String tempFileName = item.objectName();
                String finalFileName = tempFileName.replace(tempFolder, finalFolder);

                // Copy file
                minioClient.copyObject(
                        CopyObjectArgs.builder()
                                .bucket(bucketName)
                                .object(finalFileName)
                                .source(CopySource.builder().bucket(bucketName).object(tempFileName).build())
                                .build()
                );

                // Xóa file tạm
                delete(bucketName, tempFolder);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi trong quá trình đổi tên và xóa file: " + e.getMessage());
        }
    }
}

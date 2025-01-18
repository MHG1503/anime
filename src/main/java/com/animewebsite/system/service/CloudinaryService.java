package com.animewebsite.system.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public Map<String,String> uploadFile(MultipartFile multipartFile) throws IOException {
        Map result = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());

        String publicId = (String) result.get("public_id");

        // Lấy URL cho từng kích thước khác nhau
        String smallImageUrl = getResizedImageUrl(publicId, 150, 270);
        String mediumImageUrl = getResizedImageUrl(publicId, 280, 400);
        String largeImageUrl = getResizedImageUrl(publicId, 600, 720);
        String maximumImageUrl = getResizedImageUrl(publicId, 1200, 1320);

        return new HashMap<>(Map.of(
                "publicId",publicId,
                "image",result.get("url").toString(),
                "small",smallImageUrl,
                "medium",mediumImageUrl,
                "large",largeImageUrl,
                "maximum",maximumImageUrl));
    }

    public Map<String,String> basicUploadFile(MultipartFile multipartFile) throws IOException {
        Map result = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
        String publicId = (String) result.get("public_id");

        return new HashMap<>(Map.of(
                "public_id",publicId,
                "image",result.get("url").toString()));
    }

    private String getResizedImageUrl(String publicId,int width,int height){
        return cloudinary.url()
                .transformation(new Transformation()
                        .width(width)
                        .height(height)
                        .crop("fill"))
                .generate(publicId);
    }

    public Map<String, Object> deleteImage(String publicId) throws Exception {
        try {
            return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new Exception("Loi: Khong xoa duoc anh khoi Cloudinary: " + e.getMessage());
        }
    }
}

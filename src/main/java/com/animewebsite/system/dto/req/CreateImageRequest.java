package com.animewebsite.system.dto.req;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateImageRequest {
    private String imageUrl;

    private MultipartFile smallImageUrl; // Đường dẫn đến hình ảnh nhỏ

    private MultipartFile mediumImageUrl; // Đường dẫn đến hình ảnh trung bình

    private String largeImageUrl; // Đường dẫn đến hình ảnh lớn

    private String maximumImageUrl; // Đường dẫn đến hình ảnh cực lớn
}

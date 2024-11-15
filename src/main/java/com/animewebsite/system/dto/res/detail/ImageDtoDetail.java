package com.animewebsite.system.dto.res.detail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDtoDetail {
    private String imageUrl;
    private String smallImageUrl; // Đường dẫn đến hình ảnh nhỏ
    private String mediumImageUrl; // Đường dẫn đến hình ảnh trung bình
    private String largeImageUrl; // Đường dẫn đến hình ảnh lớn
    private String maximumImageUrl; // Đường dẫn đến hình ảnh cực lớn
}

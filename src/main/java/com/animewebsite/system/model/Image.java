package com.animewebsite.system.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "small_image_url")
    private String smallImageUrl; // Đường dẫn đến hình ảnh nhỏ

    @Column(name = "medium_image_url")
    private String mediumImageUrl; // Đường dẫn đến hình ảnh trung bình

    @Column(name = "large_image_url")
    private String largeImageUrl; // Đường dẫn đến hình ảnh lớn

    @Column(name = "maximum_image_url")
    private String maximumImageUrl; // Đường dẫn đến hình ảnh cực lớn

    @Column(name = "public_id")
    private String publicId;

}

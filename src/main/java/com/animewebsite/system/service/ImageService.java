package com.animewebsite.system.service;

import com.animewebsite.system.convert.ImageMapper;
import com.animewebsite.system.dto.res.lazy.ImageDtoLazy;
import com.animewebsite.system.model.Image;
import com.animewebsite.system.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageDtoLazy getImageById(Long id){
        Image image = imageRepository.findById(id).orElseThrow(()->new RuntimeException("Khong tim thay anh"));
        return imageMapper.imageToImageDtoLazy(image);
    }

}

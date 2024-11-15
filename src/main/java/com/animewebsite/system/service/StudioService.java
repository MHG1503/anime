package com.animewebsite.system.service;

import com.animewebsite.system.convert.StudioMapper;
import com.animewebsite.system.dto.req.StudioRequest;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.dto.res.lazy.StudioDtoLazy;
import com.animewebsite.system.model.Image;
import com.animewebsite.system.model.Studio;
import com.animewebsite.system.repository.StudioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;
    private final CloudinaryService cloudinaryService;
    private final StudioMapper studioMapper;

    public PaginatedResponse<StudioDtoLazy> getAllStudios(int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize, Sort.by("name"));
        Page<Studio> studios = studioRepository.findAll(pageable);

        return new PaginatedResponse<>(
                studios.getContent().stream().map(studioMapper::studioToStudioDtoLazy).toList(),
                studios.getTotalPages(),
                studios.getNumber() + 1,
                studios.getTotalElements()
        );
    }

    public StudioDtoLazy getStudioById(Long id){
        Studio studio = studioRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Khong tim thay studio"));
        return studioMapper.studioToStudioDtoLazy(studio);
    }

    @Transactional
    public StudioDtoLazy createStudio(StudioRequest studioRequest){
        String nameRequest = studioRequest.getName();
        Optional<Studio> studioOptional = studioRepository.findByName(nameRequest);
        if(studioOptional.isPresent()){
            throw new RuntimeException("Studio da ton tai!");
        }
        try {
            MultipartFile multipartFile = studioRequest.getAvatar();
            Image image = null;
            if(multipartFile != null){
                Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);
                image = Image
                        .builder()
                        .publicId(resultUploadImage.get("public_id"))
                        .imageUrl(resultUploadImage.get("image"))
                        .build();
            }

            Studio studio = Studio
                    .builder()
                    .name(nameRequest)
                    .image(image)
                    .build();
            return studioMapper.studioToStudioDtoLazy(studioRepository.save(studio));
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai");
        }
    }

    @Transactional
    public StudioDtoLazy updateStudio(Long id,
                                        StudioRequest studioRequest){
        Studio existStudio = studioRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Studio khong ton tai!"));

        try {
            MultipartFile multipartFile = studioRequest.getAvatar();
            if(multipartFile != null){
                Image image = existStudio.getImage();

                if(image != null && image.getPublicId() != null){ // truong hop studio da co anh roi (image != null)

                    cloudinaryService.deleteImage(image.getPublicId()); // xoa anh cu truoc khi upload anh moi

                    Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);
                    image.setImageUrl(resultUploadImage.get("image"));
                    image.setPublicId(resultUploadImage.get("public_id"));

                }else{ // truong hop studio chua co anh ( image == null)

                    Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);

                    image = Image
                            .builder()
                            .imageUrl(resultUploadImage.get("image"))
                            .publicId(resultUploadImage.get("public_id"))
                            .build();
                    existStudio.setImage(image);
                }
            }

            String nameRequest = studioRequest.getName();
            existStudio.setName(nameRequest);
            return studioMapper.studioToStudioDtoLazy(studioRepository.save(existStudio));

        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai");
        }
    }

    @Transactional
    public void deleteStudio(Long id) {
        Studio existStudio = studioRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Studio khong ton tai!"));
        try {
            Image existImage = existStudio.getImage();
            if (existImage != null && existImage.getPublicId() != null) {
                cloudinaryService.deleteImage(existImage.getPublicId());
            }
            studioRepository.delete(existStudio);
        }catch (Exception e){
            throw new RuntimeException("Xoa studio that bai!");
        }
    }
}

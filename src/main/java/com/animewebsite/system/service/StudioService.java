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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;
    private final CloudinaryService cloudinaryService;
    private final StudioMapper studioMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public Object getAllStudios(int pageNum, int pageSize){
        if(pageNum != 0 && pageSize != 0) {
            Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("name"));
            Page<Studio> studios = studioRepository.findAll(pageable);

            return new PaginatedResponse<>(
                    studios.getContent().stream().map(studioMapper::studioToStudioDtoLazy).toList(),
                    studios.getTotalPages(),
                    studios.getNumber() + 1,
                    studios.getTotalElements()
            );
        }
        return studioRepository.findAll().stream().map(studioMapper::studioToStudioDtoLazy).toList();
    }

    public StudioDtoLazy getStudioById(Long id){
        Studio studio = studioRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Khong tim thay studio"));
        return studioMapper.studioToStudioDtoLazy(studio);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public StudioDtoLazy createStudio(StudioRequest studioRequest){
        String nameRequest = studioRequest.getName();
        Optional<Studio> studioOptional = studioRepository.findByName(nameRequest);
        if(studioOptional.isPresent()){
            throw new RuntimeException("Studio da ton tai!");
        }
        Image image = null;
        try {
            MultipartFile multipartFile = studioRequest.getAvatar();
            if(multipartFile != null && !multipartFile.isEmpty()){
                Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);
                image = Image
                        .builder()
                        .publicId(resultUploadImage.get("public_id"))
                        .imageUrl(resultUploadImage.get("image"))
                        .build();
            }

            String introduceRequest = studioRequest.getIntroduce();
            String dateString = studioRequest.getDate();
            Studio studio = Studio
                    .builder()
                    .name(nameRequest)
                    .introduce(introduceRequest)
                    .established(LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE))
                    .image(image)
                    .build();
            return studioMapper.studioToStudioDtoLazy(studioRepository.save(studio));
        }catch (IOException ioException){
            throw new RuntimeException("Upload anh that bai");
        }catch (Exception e){
            if (image != null) {
                try {
                    cloudinaryService.deleteImage(image.getPublicId());
                } catch (Exception ex) {
                    throw new RuntimeException("Xoa anh that bai khi them du lieu cua studio vao DB that bai!");
                }
            }
            throw new RuntimeException("Them du lieu studio that bai!");
        }
    }

    //TODO chua xy ly duoc phan anh neu update that bai!
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public StudioDtoLazy updateStudio(Long id,
                                        StudioRequest studioRequest){
        Studio existStudio = studioRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Studio khong ton tai!"));

        try {
            MultipartFile multipartFile = studioRequest.getAvatar();
            if(multipartFile != null && !multipartFile.isEmpty()){
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
            String introduceRequest = studioRequest.getIntroduce();
            String dateString = studioRequest.getDate();

            existStudio.setName(nameRequest);
            existStudio.setIntroduce(introduceRequest);
            existStudio.setEstablished(LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE));
            return studioMapper.studioToStudioDtoLazy(studioRepository.save(existStudio));

        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudio(Long id) {
        Studio existStudio = studioRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Studio khong ton tai!"));

        studioRepository.delete(existStudio);
        try {
            Image existImage = existStudio.getImage();
            if (existImage != null && existImage.getPublicId() != null) {
                cloudinaryService.deleteImage(existImage.getPublicId());
            }
        }catch (Exception e){
            throw new RuntimeException("Xoa studio that bai!");
        }
    }
}

package com.animewebsite.system.service;

import com.animewebsite.system.convert.ProducerMapper;
import com.animewebsite.system.dto.req.ProducerRequest;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.dto.res.lazy.ProducerDtoLazy;
import com.animewebsite.system.model.Image;
import com.animewebsite.system.model.Producer;
import com.animewebsite.system.repository.ProducerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerRepository producerRepository;
    private final ProducerMapper producerMapper;
    private final CloudinaryService cloudinaryService;

    @PreAuthorize("hasRole('ADMIN')")
    public Object getAllProducers(int pageNum, int pageSize){
        if(pageNum != 0 && pageSize != 0) {
            Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("name"));
            Page<Producer> producers = producerRepository
                    .findAll(pageable);

            return new PaginatedResponse<>(
                    producers.getContent().stream().map(producerMapper::producerToProducerDtoLazy).toList(),
                    producers.getTotalPages(),
                    producers.getNumber() + 1,
                    producers.getTotalElements()
            );
        }
        return producerRepository.findAll().stream().map(producerMapper::producerToProducerDtoLazy).toList();

    }

    public ProducerDtoLazy getProducerById(Long id){
        return producerMapper.producerToProducerDtoLazy(producerRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Producer khong ton tai!")));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProducerDtoLazy createProducer(ProducerRequest producerRequest){
        String nameRequest = producerRequest.getName();
        Optional<Producer> producerOptional = producerRepository.findByName(nameRequest);
        if(producerOptional.isPresent()){
            throw new RuntimeException("Producer da ton tai!");
        }
        try {
            MultipartFile multipartFile = producerRequest.getAvatar();
            Image image = null;
            if(multipartFile != null && !multipartFile.isEmpty()){
                Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);

                 image = Image
                        .builder()
                         .publicId(resultUploadImage.get("public_id"))
                        .imageUrl(resultUploadImage.get("image"))
                        .build();
            }
            String introduceRequest = producerRequest.getIntroduce();
            String dateString = producerRequest.getDate();

            Producer producer = Producer
                    .builder()
                    .name(nameRequest)
                    .introduce(introduceRequest)
                    .established(LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE))
                    .image(image)
                    .build();
            return producerMapper.producerToProducerDtoLazy(producerRepository.save(producer));
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai");
        }
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProducerDtoLazy updateProducer(Long id,ProducerRequest producerRequest){
        Producer existProducer = producerRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Producer khong ton tai!"));

        try {
            MultipartFile multipartFile = producerRequest.getAvatar();

            if(multipartFile != null && !multipartFile.isEmpty()){
                Image image = existProducer.getImage();

                if(image != null && image.getPublicId() != null){ // truong hop producer da co anh roi ( image != null)

                    cloudinaryService.deleteImage(image.getPublicId());  // xoa anh cu truoc khi upload anh moi

                    Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);
                    image.setImageUrl(resultUploadImage.get("image"));
                    image.setPublicId(resultUploadImage.get("public_id"));

                }else{ // truong hop producer chua co anh ( image == null)

                    Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);

                    image = Image
                            .builder()
                            .imageUrl(resultUploadImage.get("image"))
                            .publicId( resultUploadImage.get("public_id"))
                            .build();
                    existProducer.setImage(image);
                }
            }

            String nameRequest = producerRequest.getName();
            String introduceRequest = producerRequest.getIntroduce();
            String dateString = producerRequest.getDate();

            existProducer.setName(nameRequest);
            existProducer.setIntroduce(introduceRequest);
            existProducer.setEstablished(LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE));
            return producerMapper.producerToProducerDtoLazy(producerRepository.save(existProducer));
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProducer(Long id){
        Producer existProducer = producerRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Producer khong ton tai!"));
        try {
            Image existImage = existProducer.getImage();
            if (existImage != null && existImage.getPublicId() != null) {
                cloudinaryService.deleteImage(existImage.getPublicId());
            }
            producerRepository.delete(existProducer);
        }catch (Exception e){
            throw new RuntimeException("Xoa producer that bai!");
        }
    }

}

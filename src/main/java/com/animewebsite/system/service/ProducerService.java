package com.animewebsite.system.service;

import com.animewebsite.system.convert.ProducerMapper;
import com.animewebsite.system.dto.req.ProducerRequest;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.dto.res.detail.ProducerDtoDetail;
import com.animewebsite.system.dto.res.lazy.ProducerDtoLazy;
import com.animewebsite.system.model.Image;
import com.animewebsite.system.model.Producer;
import com.animewebsite.system.repository.ProducerRepository;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
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
public class ProducerService {
    private final ProducerRepository producerRepository;
    private final ProducerMapper producerMapper;
    private final CloudinaryService cloudinaryService;

    public PaginatedResponse<ProducerDtoLazy> getAllProducers(int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize, Sort.by("name"));
        Page<Producer> producers = producerRepository.findAll(pageable,NamedEntityGraph.fetching("producer-with-image"));

        return new PaginatedResponse<>(
                producers.getContent().stream().map(producerMapper::producerToProducerDtoLazy).toList(),
                producers.getTotalPages(),
                producers.getNumber() + 1,
                producers.getTotalElements()
        );
    }

    public ProducerDtoDetail getProducerById(Long id){
        return producerMapper.producerToProducerDtoDetail(producerRepository
                .findById(id, NamedEntityGraph.fetching("producer-with-anime-and-image"))
                .orElseThrow(()->new RuntimeException("Producer khong ton tai!")));
    }

    @Transactional
    public ProducerDtoDetail createProducer(ProducerRequest producerRequest, MultipartFile multipartFile){
        String nameRequest = producerRequest.getName();
        Optional<Producer> producerOptional = producerRepository.findByName(nameRequest);
        if(producerOptional.isPresent()){
            throw new RuntimeException("Producer da ton tai!");
        }
        try {
            Image image = null;
            if(multipartFile != null){
                Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);

                 image = Image
                        .builder()
                         .publicId(resultUploadImage.get("public_id"))
                        .imageUrl(resultUploadImage.get("image"))
                        .build();
            }

            Producer producer = Producer
                    .builder()
                    .name(nameRequest)
                    .image(image)
                    .build();
            return producerMapper.producerToProducerDtoDetail(producerRepository.save(producer));
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai");
        }
    }


    @Transactional
    public ProducerDtoDetail updateProducer(Long id,ProducerRequest producerRequest,MultipartFile multipartFile){
        Producer existProducer = producerRepository
                .findById(id,NamedEntityGraph.fetching("producer-with-anime-and-image"))
                .orElseThrow(()->new RuntimeException("Producer khong ton tai!"));

        try {
            if(multipartFile != null){
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
            existProducer.setName(nameRequest);
            return producerMapper.producerToProducerDtoDetail(producerRepository.save(existProducer));
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai");
        }
    }

    @Transactional
    public void deleteProducer(Long id){
        Producer existProducer = producerRepository
                .findById(id, NamedEntityGraph.fetching("producer-with-anime-and-image"))
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

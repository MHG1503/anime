package com.animewebsite.system.service;

import com.animewebsite.system.convert.AnimeCharacterVoiceActorMapper;
import com.animewebsite.system.convert.VoiceActorMapper;
import com.animewebsite.system.dto.req.VoiceActorRequest;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.dto.res.lazy.VoiceActorDtoLazy;
import com.animewebsite.system.model.Image;
import com.animewebsite.system.model.VoiceActor;
import com.animewebsite.system.model.enums.Nationality;
import com.animewebsite.system.repository.AnimeCharacterVoiceActorRepository;
import com.animewebsite.system.repository.VoiceActorRepository;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class VoiceActorService {
    private final VoiceActorRepository voiceActorRepository;
    private final VoiceActorMapper voiceActorMapper;
    private final CloudinaryService cloudinaryService;
    private final AnimeCharacterVoiceActorRepository animeCharacterVoiceActorRepository;
    private final AnimeCharacterVoiceActorMapper animeCharacterVoiceActorMapper;

    public PaginatedResponse<VoiceActorDtoLazy> getAllVoiceActors(Integer pageNum, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        Page<VoiceActor> voiceActorPage = voiceActorRepository.findAll(pageable);

        return new PaginatedResponse<>(
                voiceActorPage.getContent().stream().map(voiceActorMapper::voiceActorToVoiceActorDtoLazy).toList(),
                voiceActorPage.getTotalPages(),
                voiceActorPage.getNumber()+1,
                voiceActorPage.getTotalElements()
        );
    }

    public VoiceActorDtoLazy getVoiceActorById(Long id){
        VoiceActor voiceActor = voiceActorRepository
                .findById(id,NamedEntityGraph.fetching("voice_actor-with-image"))
                .orElseThrow(()->new RuntimeException("Khong tim thay voice actor voi id la: " + id));

        return voiceActorMapper.voiceActorToVoiceActorDtoLazy(voiceActor);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Object getAllVoiceActorSelections(){
        return voiceActorRepository
                .findAll()
                .stream()
                .map(voiceActorMapper::voiceActorToVoiceActorDtoSelect)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public VoiceActorDtoLazy createVoiceActor(VoiceActorRequest voiceActorRequest, MultipartFile multipartFile){
        String nameRequest = voiceActorRequest.getName();
        Optional<VoiceActor> voiceActorOptional = voiceActorRepository.findByName(nameRequest);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if(voiceActorOptional.isPresent()){
            throw new RuntimeException("Voice actor: " + nameRequest + " nay da ton tai!");
        }

        Image image = null;
        try{

            if(multipartFile != null){
                Map<String,String> map = cloudinaryService.basicUploadFile(multipartFile);

                image = Image
                        .builder()
                        .imageUrl(map.get("image"))
                        .publicId(map.get("public_id"))
                        .build();
            }

            String aboutRequest = voiceActorRequest.getAbout();
            String malUrl = voiceActorRequest.getUrl();
            LocalDate dob = LocalDate.parse(voiceActorRequest.getDob(),formatter);
            String nationality = voiceActorRequest.getNationality().getName();

            VoiceActor voiceActor = VoiceActor
                    .builder()
                    .name(nameRequest)
                    .image(image)
                    .about(aboutRequest)
                    .url(malUrl)
                    .dob(dob)
                    .nationality(Nationality.valueOf(nationality))
                    .build();
            return voiceActorMapper.voiceActorToVoiceActorDtoLazy(voiceActorRepository.save(voiceActor));
        }catch (IOException ioException){
            throw new RuntimeException("Upload anh that bai!");
        }catch (Exception e){
            if(image != null){
                try {
                    cloudinaryService.deleteImage(image.getPublicId());
                } catch (Exception ex) {
                    throw new RuntimeException("Xoa anh khong thong cong sau khi khong them duoc du lieu voice actor vao DB!");
                }
            }
            throw new RuntimeException("Them voice actor that bai!");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public VoiceActorDtoLazy updateVoiceActor(Long id,
                                              VoiceActorRequest voiceActorRequest,
                                              MultipartFile multipartFile){
        VoiceActor existVoiceActor = voiceActorRepository
                .findById(id,NamedEntityGraph.fetching("voice_actor-with-image"))
                .orElseThrow(()-> new RuntimeException("Khong tim thay voice actor id: " + id));

        try {
            if(multipartFile != null) {
                Image image = existVoiceActor.getImage();

                if (image != null && image.getPublicId() != null) { // truong hop nguoi long tieng (voice actor) da co image roi ( image != null)

                    cloudinaryService.deleteImage(image.getPublicId()); // xoa anh cu truoc khi upload anh moi

                    Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);
                    image.setImageUrl(resultUploadImage.get("image"));
                    image.setPublicId(resultUploadImage.get("public_id"));

                }else{  // truong hop  nguoi long tieng (voice actor) chua co image ( image == null)

                    Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);

                    image = Image
                            .builder()
                            .imageUrl(resultUploadImage.get("image"))
                            .publicId(resultUploadImage.get("public_id"))
                            .build();
                    existVoiceActor.setImage(image);
                }
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String nameRequest = voiceActorRequest.getName();
            String aboutRequest = voiceActorRequest.getAbout();
            String malUrl = voiceActorRequest.getUrl();
            LocalDate dob = LocalDate.parse(voiceActorRequest.getDob(),formatter);
            String nationality = voiceActorRequest.getNationality().getName();

            existVoiceActor.setName(nameRequest);
            existVoiceActor.setAbout(aboutRequest);
            existVoiceActor.setUrl(malUrl);
            existVoiceActor.setDob(dob);
            existVoiceActor.setNationality(Nationality.valueOf(nationality));

            return voiceActorMapper.voiceActorToVoiceActorDtoLazy(voiceActorRepository.save(existVoiceActor));
        }catch (IOException ioException){
            throw new RuntimeException("Upload anh that bai");
        }catch (Exception e){
            throw new RuntimeException("Xoa anh that bai truoc khi thay the bang anh moi!");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteVoiceActor(Long id){
        VoiceActor existVoiceActor = voiceActorRepository
                .findById(id,NamedEntityGraph.fetching("voice_actor-with-image"))
                .orElseThrow(()-> new RuntimeException("Khong tim thay voice actor id: " + id));
        try{
            Image existImage = existVoiceActor.getImage();
            if (existImage != null && existImage.getPublicId() != null) {
                cloudinaryService.deleteImage(existImage.getPublicId());
            }
            animeCharacterVoiceActorRepository.detachVoiceActorFromCharacters(existVoiceActor.getId());

            voiceActorRepository.delete(existVoiceActor);
            voiceActorMapper.voiceActorToVoiceActorDtoLazy(existVoiceActor);
        }catch (Exception e){
            throw new RuntimeException("Xoa anh that bai");
        }
    }
}

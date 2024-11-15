package com.animewebsite.system.service;

import com.animewebsite.system.convert.AnimeCharacterVoiceActorMapper;
import com.animewebsite.system.convert.CharacterMapper;
import com.animewebsite.system.dto.req.CharacterRequest;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.dto.res.detail.CharacterDtoDetail;
import com.animewebsite.system.dto.res.lazy.CharacterDtoLazy;
import com.animewebsite.system.model.Character;
import com.animewebsite.system.model.Image;
import com.animewebsite.system.repository.CharacterRepository;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CloudinaryService cloudinaryService;
    private final AnimeCharacterVoiceActorMapper animeCharacterVoiceActorMapper;
    private final CharacterMapper characterMapper;

    public PaginatedResponse<CharacterDtoLazy> getAllCharacters(Integer pageNum, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize);
        Page<Character> characters = characterRepository.findAll(pageable);

        return new PaginatedResponse<>(
                characters.getContent().stream().map(characterMapper::characterToCharacterDtoLazy).toList(),
                characters.getTotalPages(),
                characters.getNumber() + 1,
                characters.getTotalElements()
        );
    }

    public CharacterDtoDetail getCharacterById(Long id){
        return animeCharacterVoiceActorMapper.characterToCharacterDtoDetail(characterRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Khong tim thay character!")));
    }

    @Transactional
    public CharacterDtoLazy createCharacter(CharacterRequest characterRequest,
                                            MultipartFile multipartFile){
        String nameRequest = characterRequest.getName();
        Optional<Character> characterOptional = characterRepository.findByName(nameRequest);
        if(characterOptional.isPresent()){
            throw new RuntimeException("Nhan vat (character) da ton tai!");
        }
        Image image = null;
        try {
            if(multipartFile != null){
                Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);

                image = Image
                        .builder()
                        .imageUrl(resultUploadImage.get("image"))
                        .publicId(resultUploadImage.get("public_id"))
                        .build();
            }

            String aboutRequest = characterRequest.getAbout();

            Character character = Character
                    .builder()
                    .name(nameRequest)
                    .about(aboutRequest)
                    .image(image)
                    .build();
            return characterMapper.characterToCharacterDtoLazy(characterRepository.save(character));
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai");
        }
    }

    @Transactional
    public CharacterDtoLazy updateCharacter(Long id,
                                            CharacterRequest characterRequest,
                                            MultipartFile multipartFile){
        Character existCharacter = characterRepository
                .findById(id,NamedEntityGraph.fetching("character-with-anime-and-voice_actor"))
                .orElseThrow(()->new RuntimeException("Khong tim thay nhan vat (character)! id: " + id));

        try {
            if(multipartFile != null){
                Image image = existCharacter.getImage();

                if(image != null && image.getPublicId() != null){ // truong hop nhan vat (character) da co image roi! (image != null)

                    cloudinaryService.deleteImage(image.getPublicId()); // xoa anh cu truoc khi upload anh moi

                    Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);
                    image.setImageUrl(resultUploadImage.get("image"));
                    image.setPublicId(resultUploadImage.get("public_id"));
                }else{ // truong hop nhan vat ( character) chua co image ( image == null)
                    Map<String,String> resultUploadImage = cloudinaryService.basicUploadFile(multipartFile);

                    image = Image
                            .builder()
                            .imageUrl(resultUploadImage.get("image"))
                            .publicId(resultUploadImage.get("public_id"))
                            .build();
                    existCharacter.setImage(image);
                }
            }
            String nameRequest = characterRequest.getName();
            String aboutRequest = characterRequest.getAbout();
            existCharacter.setName(nameRequest);
            existCharacter.setAbout(aboutRequest);

            return characterMapper.characterToCharacterDtoLazy(characterRepository.save(existCharacter));
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai!");
        }
    }

    @Transactional
    public CharacterDtoLazy deleteCharacterById(Long id){
        Character character = characterRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Khong tim thay nhan vat ( character) id: " + id));

        try {
            if (character.getImage() != null && character.getImage().getPublicId() != null) {
                cloudinaryService.deleteImage(character.getImage().getPublicId());
            }
        }catch (Exception e){
            throw new RuntimeException("Xoa anh that bai!");
        }

        characterRepository.delete(character);
        return characterMapper.characterToCharacterDtoLazy(character);
    }
}

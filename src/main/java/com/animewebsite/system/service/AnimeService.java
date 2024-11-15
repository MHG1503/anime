package com.animewebsite.system.service;

import com.animewebsite.system.convert.AnimeMapper;
import com.animewebsite.system.dto.req.CreateAnimeRequest;
import com.animewebsite.system.dto.req.GenreRequest;
import com.animewebsite.system.dto.req.UpdateAnimeRequest;
import com.animewebsite.system.dto.res.detail.AnimeDtoDetail;
import com.animewebsite.system.dto.res.lazy.AnimeDtoLazy;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.model.*;
import com.animewebsite.system.model.enums.Season;
import com.animewebsite.system.model.enums.Status;
import com.animewebsite.system.model.enums.Type;
import com.animewebsite.system.repository.*;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final AnimeMapper animeMapper;
    private final AnimeCharacterVoiceActorRepository animeCharacterVoiceActorRepository;
    private final CloudinaryService cloudinaryService;

    public PaginatedResponse<AnimeDtoLazy> getAllAnime(int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize);

        Page<Anime> animePage = animeRepository.findAll(pageable);

        return new PaginatedResponse<>(
                animePage.getContent().stream().map(animeMapper::animeToAnimeDtoLazy).toList(),
                animePage.getTotalPages(),
                animePage.getNumber() + 1,
                animePage.getTotalElements()
        );
    }

    public AnimeDtoDetail getAnimeById(Long id){
        return animeMapper.animeToAnimeDtoDetail(
                animeRepository
                        .findById(id, NamedEntityGraph.fetching("anime-with-alternative_name-image-genre-producer-studio"))
                        .orElseThrow(()->new RuntimeException("Khong tim thay anime voi id: " + id)));
    }

    @Transactional
    public AnimeDtoLazy createAnime(CreateAnimeRequest createAnimeRequest, MultipartFile multipartFile){
        String nameRequest = createAnimeRequest.getName();
        Set<AlternativeTitle> alternativeTitle = createAnimeRequest.getAlternativeTitles();
        String descriptionRequest = createAnimeRequest.getDescription();
        Status statusRequest = createAnimeRequest.getStatus();
        Type typeRequest = createAnimeRequest.getType();
        Aired airedRequest = createAnimeRequest.getAired();
        Double durationRequest = createAnimeRequest.getDuration();
        Double malScoreRequest = createAnimeRequest.getMalScore();
        Season seasonRequest = createAnimeRequest.getSeason();
        Integer yearRequest = createAnimeRequest.getYear();
        Integer episodesRequest = createAnimeRequest.getEpisodes();
        Set<Long> genresIdsRequest = createAnimeRequest.getGenresIds();
        Set<Long> producersIdsRequest = createAnimeRequest.getProducersIds();
        Set<Long> studiosIdsRequest = createAnimeRequest.getStudiosIds();

        Optional<Anime> animeOptional = animeRepository.findByName(nameRequest);
        if(animeOptional.isPresent()){
            throw new RuntimeException("Anime da ton tai roi");
        }
        Image image = null;
        try {
            Map<String, String> imagesUrl = cloudinaryService.uploadFile(multipartFile);

            //TODO: handle image;
            image = Image
                    .builder()
                    .imageUrl(imagesUrl.get("image"))
                    .smallImageUrl(imagesUrl.get("small"))
                    .mediumImageUrl(imagesUrl.get("medium"))
                    .largeImageUrl(imagesUrl.get("large"))
                    .mediumImageUrl(imagesUrl.get("maximum"))
                    .build();
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai!");
        }
        Anime anime = Anime
                .builder()
                .name(nameRequest)
                .alternativeTitles(alternativeTitle)
                .description(descriptionRequest)
                .status(statusRequest)
                .type(typeRequest)
                .aired(airedRequest)
                .duration(durationRequest)
                .malScore(malScoreRequest)
                .season(seasonRequest)
                .year(yearRequest)
                .episodes(episodesRequest)
                .image(image)
//                .genres(genresRequest)
//                .producers(producersRequest)
//                .studios(studiosRequest)
                .genres(new HashSet<>())
                .producers(new HashSet<>())
                .studios(new HashSet<>())
                .build();
        return animeMapper.animeToAnimeDtoLazy(animeRepository.save(anime));
    }

    @Transactional
    public AnimeDtoLazy updateAnime(Long id, UpdateAnimeRequest updateAnimeRequest){
        String nameRequest = updateAnimeRequest.getName();
        String descriptionRequest = updateAnimeRequest.getDescription();
        Status statusRequest = updateAnimeRequest.getStatus();
        Type typeRequest = updateAnimeRequest.getType();
        Aired airedRequest = updateAnimeRequest.getAired();
        Double durationRequest = updateAnimeRequest.getDuration();
        Double malScoreRequest = updateAnimeRequest.getMalScore();
        Season seasonRequest = updateAnimeRequest.getSeason();
        Integer yearRequest = updateAnimeRequest.getYear();
        Integer episodesRequest = updateAnimeRequest.getEpisodes();

        Anime existAnime = animeRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Không tìm thấy anime"));

        existAnime.setName(nameRequest);
        existAnime.setDescription(descriptionRequest);
        existAnime.setStatus(statusRequest);
        existAnime.setType(typeRequest);
        existAnime.setDuration(durationRequest);
        existAnime.setMalScore(malScoreRequest);
        existAnime.setSeason(seasonRequest);
        existAnime.setYear(yearRequest);
        existAnime.setEpisodes(episodesRequest);
        existAnime.setAired(airedRequest);
        return animeMapper.animeToAnimeDtoLazy(animeRepository.save(existAnime));
    }

    @Transactional
    public AnimeDtoLazy deleteAnimeById(Long id){
        Anime anime = animeRepository
                .findById(id, NamedEntityGraph.fetching("anime-with-alternative_name-image-genre-producer-studio"))
                .orElseThrow(()-> new RuntimeException("Khong tim thay anime voi id: " + id));

        try {
            Image image = anime.getImage();
            if(image != null && image.getPublicId() != null){
                cloudinaryService.deleteImage(image.getPublicId());
            }
        }catch (Exception e){
            throw new RuntimeException("Xoa anh that bai!");
        }

        animeCharacterVoiceActorRepository.deleteByAnimeCharacterVoiceActorIdAnimeId(anime.getId());

        animeRepository.delete(anime);

        return animeMapper.animeToAnimeDtoLazy(anime);
    }
}

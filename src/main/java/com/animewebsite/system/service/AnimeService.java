package com.animewebsite.system.service;

import com.animewebsite.system.convert.AnimeMapper;
import com.animewebsite.system.convert.EpisodeMapper;
import com.animewebsite.system.dto.req.AlternativeTitleRequest;
import com.animewebsite.system.dto.req.CreateAnimeRequest;
import com.animewebsite.system.dto.req.UpdateAnimeRequest;
import com.animewebsite.system.dto.res.VideosResponse;
import com.animewebsite.system.dto.res.detail.AnimeDtoDetail;
import com.animewebsite.system.dto.res.lazy.AnimeDtoLazy;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.model.*;
import com.animewebsite.system.model.Character;
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
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final AnimeMapper animeMapper;
    private final AnimeCharacterVoiceActorRepository animeCharacterVoiceActorRepository;
    private final SeriesRepository seriesRepository;
    private final GenreRepository genreRepository;
    private final ProducerRepository producerRepository;
    private final StudioRepository studioRepository;
    private final EpisodeRepository episodeRepository;
    private final PromotionVideoRepository promotionVideoRepository;
    private final AlternativeTitleRepository alternativeTitleRepository;
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

    public PaginatedResponse<AnimeDtoLazy> getAllAnimeByGenre(int pageNum, int pageSize, String genreName){
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize);

        Page<Anime> animePage = animeRepository.findByGenresName(List.of(genreName),pageable);

        return new PaginatedResponse<>(
                animePage.getContent().stream().map(animeMapper::animeToAnimeDtoLazy).toList(),
                animePage.getTotalPages(),
                animePage.getNumber() + 1,
                animePage.getTotalElements()
        );
    }

    public PaginatedResponse<AnimeDtoLazy> getAllRecommendAnime(int pageNum, int pageSize, List<String> genresName){
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize);

        Page<Anime> animePage = animeRepository.findByGenresName(genresName,pageable);

        return new PaginatedResponse<>(
                animePage.getContent().stream().map(animeMapper::animeToAnimeDtoLazy).toList(),
                animePage.getTotalPages(),
                animePage.getNumber() + 1,
                animePage.getTotalElements()
        );
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Co the bo ham nay
    public VideosResponse getAnimeVideos(Long id, int pageNum, int pageSize){

        Page<Episode> episodes = episodeRepository
                .findAllByAnimeId(id,PageRequest.of(pageNum - 1,pageSize,Sort.by("episode")));

        Page<PromotionVideo> promotionVideos = promotionVideoRepository
                .findAllByAnimeId(id,PageRequest.of(1, 10, Sort.by("createDate")));
        return VideosResponse
                .builder()
                .episodes(new PaginatedResponse<>(
                            episodes.getContent(),
                            episodes.getTotalPages(),
                            episodes.getNumber(),
                            episodes.getTotalElements()))
                .promtionVideos(new PaginatedResponse<>(
                        promotionVideos.getContent(),
                        promotionVideos.getTotalPages(),
                        promotionVideos.getNumber(),
                        promotionVideos.getTotalElements()))
                .build();
    }
    ////////////////////////////////////////////////////////////////////////////////

    public Object getAnimeEpisodes(Long id, int pageNum, int pageSize){
        Page<Episode> episodes = episodeRepository
                .findAllByAnimeId(id,PageRequest.of(pageNum - 1,pageSize,Sort.by("episode")));
        return new PaginatedResponse<>(
                episodes.getContent(),
                episodes.getTotalPages(),
                episodes.getNumber(),
                episodes.getTotalElements());
    }

    public Object getAnimeEpisodeById(Long animeId, Long episodeId){
        return episodeRepository
                .findByIdAndAnimeId(episodeId,animeId)
                .orElseThrow(()->new RuntimeException("Khong tim thay episode id" + episodeId));
    }

    public PaginatedResponse<AnimeDtoLazy> getAllAnimeBySeriesId(int pageNum, int pageSize, Long seriesId){
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize);

        Page<Anime> animePage = null;
        if(seriesId != null){
            Series series = seriesRepository
                    .findById(seriesId)
                    .orElseThrow(()-> new RuntimeException("Khong tim thay series nay!"));

            animePage = animeRepository.findBySeriesId(series.getId(),pageable);
        }else{
            animePage = animeRepository.findBySeriesIdIsNull(pageable);

        }

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
    @PreAuthorize("hasRole('ADMIN')")
    public AnimeDtoLazy createAnime(CreateAnimeRequest createAnimeRequest, MultipartFile multipartFile){
        String nameRequest = createAnimeRequest.getName();

        Set<AlternativeTitleRequest> alternativeTitleRequests = createAnimeRequest.getAlternativeTitles();
        Set<AlternativeTitle> alternativeTitles = new HashSet<>();

        // handle alternative title request
        for(var request : alternativeTitleRequests){
            Optional<AlternativeTitle> alternativeTitleOptional = alternativeTitleRepository
                    .findByAlternativeName(request.getAlternativeName());
            if(alternativeTitleOptional.isPresent()){
                throw new RuntimeException("Ten : " + request.getAlternativeName() + " da bi trung");
            }
            alternativeTitles.add(AlternativeTitle
                    .builder()
                            .id(null)
                            .alternativeName(request.getAlternativeName())
                            .language(request.getLanguage())
                    .build());
        }

        String descriptionRequest = createAnimeRequest.getDescription();
        Status statusRequest = createAnimeRequest.getStatus();
        Type typeRequest = createAnimeRequest.getType();
        Aired airedRequest = createAnimeRequest.getAired();
        Double durationRequest = createAnimeRequest.getDuration();
        Double malScoreRequest = createAnimeRequest.getMalScore();
        Season seasonRequest = createAnimeRequest.getSeason();
        Integer yearRequest = createAnimeRequest.getYear();
        Integer episodesRequest = createAnimeRequest.getEpisodes();
        Long seriesId = createAnimeRequest.getSeriesId();

        //handle series request
        Series series = seriesRepository
                .findById(seriesId)
                .orElseThrow(()-> new RuntimeException("Khong tim thay series!"));

        // handle genres request
        Set<Long> genresIdsRequest = createAnimeRequest.getGenresIds();
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genresIdsRequest));

        // handle producers request
        Set<Long> producersIdsRequest = createAnimeRequest.getProducersIds();
        Set<Producer> producers = new HashSet<>(producerRepository.findAllById(producersIdsRequest));

        // handle studio request
        Set<Long> studiosIdsRequest = createAnimeRequest.getStudiosIds();
        Set<Studio> studios = new HashSet<>(studioRepository.findAllById(studiosIdsRequest));

        Optional<Anime> animeOptional = animeRepository.findByName(nameRequest);
        if(animeOptional.isPresent()){
            throw new RuntimeException("Anime da ton tai roi");
        }

        // handle image
        try {
            Image image = null;
            if (multipartFile != null && !multipartFile.isEmpty()) {
                Map<String, String> imagesUrl = cloudinaryService.uploadFile(multipartFile);

                image = Image
                        .builder()
                        .imageUrl(imagesUrl.get("image"))
                        .smallImageUrl(imagesUrl.get("small"))
                        .mediumImageUrl(imagesUrl.get("medium"))
                        .largeImageUrl(imagesUrl.get("large"))
                        .mediumImageUrl(imagesUrl.get("maximum"))
                        .publicId(imagesUrl.get("publicId"))
                        .build();
            }
            Anime anime = Anime
                    .builder()
                    .name(nameRequest)
                    .alternativeTitles(alternativeTitles)
                    .description(descriptionRequest)
                    .status(statusRequest)
                    .type(typeRequest)
                    .series(series)
                    .aired(airedRequest)
                    .duration(durationRequest)
                    .malScore(malScoreRequest)
                    .season(seasonRequest)
                    .year(yearRequest)
                    .episodes(episodesRequest)
                    .image(image)
                    .genres(genres)
                    .producers(producers)
                    .studios(studios)
                    .build();

            return animeMapper.animeToAnimeDtoLazy(animeRepository.save(anime));
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public AnimeDtoLazy updateAnime(Long id, UpdateAnimeRequest updateAnimeRequest,MultipartFile multipartFile){

        Anime existAnime = animeRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Không tìm thấy anime"));

        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                Image image = existAnime.getImage();

                if (image != null && image.getPublicId() != null) { // truong hop studio da co anh roi (image != null)

                    cloudinaryService.deleteImage(image.getPublicId()); // xoa anh cu truoc khi upload anh moi

                    Map<String, String> imagesUrl = cloudinaryService.uploadFile(multipartFile);
                    image.setImageUrl(imagesUrl.get("image"));
                    image.setSmallImageUrl(imagesUrl.get("small"));
                    image.setMediumImageUrl(imagesUrl.get("medium"));
                    image.setLargeImageUrl(imagesUrl.get("large"));
                    image.setMaximumImageUrl(imagesUrl.get("maximum"));
                    image.setPublicId(imagesUrl.get("public_id"));

                } else { // truong hop studio chua co anh ( image == null)

                    Map<String, String> imagesUrl = cloudinaryService.uploadFile(multipartFile);

                    image = Image
                            .builder()
                            .imageUrl(imagesUrl.get("image"))
                            .smallImageUrl(imagesUrl.get("small"))
                            .mediumImageUrl(imagesUrl.get("medium"))
                            .largeImageUrl(imagesUrl.get("large"))
                            .maximumImageUrl(imagesUrl.get("maximum"))
                            .publicId(imagesUrl.get("publicId"))
                            .build();
                    existAnime.setImage(image);
                }
            }
        }catch (Exception e){
            throw new RuntimeException("Upload anh that bai!");
        }

        Set<AlternativeTitleRequest> alternativeTitleRequests = updateAnimeRequest.getAlternativeTitles();
        Set<AlternativeTitle> alternativeTitles = new HashSet<>();

        //handle alternative title request
        for(var request : alternativeTitleRequests){
            Optional<AlternativeTitle> alternativeTitleOptional = alternativeTitleRepository
                    .findById(request.getId());
            AlternativeTitle alternativeTitle = null;
            if(alternativeTitleOptional.isPresent()){
                alternativeTitle = alternativeTitleOptional.get();
                alternativeTitle.setAlternativeName(request.getAlternativeName());
                alternativeTitle.setLanguage(request.getLanguage());
            }else{
                alternativeTitle = AlternativeTitle
                        .builder()
                        .id(null)
                        .alternativeName(request.getAlternativeName())
                        .language(request.getLanguage())
                        .build();
            }
            alternativeTitles.add(alternativeTitle);
        }

        Long seriesId = updateAnimeRequest.getSeriesId();
        //handle series request
        Series series = seriesRepository
                .findById(seriesId)
                .orElseThrow(()-> new RuntimeException("Khong tim thay series!"));

        // handle genres request
        Set<Long> genresIdsRequest = updateAnimeRequest.getGenresIds();
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genresIdsRequest));

        // handle producers request
        Set<Long> producersIdsRequest = updateAnimeRequest.getProducersIds();
        Set<Producer> producers = new HashSet<>(producerRepository.findAllById(producersIdsRequest));

        // handle studio request
        Set<Long> studiosIdsRequest = updateAnimeRequest.getStudiosIds();
        Set<Studio> studios = new HashSet<>(studioRepository.findAllById(studiosIdsRequest));

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

        existAnime.setName(nameRequest);
        existAnime.setDescription(descriptionRequest);
        existAnime.setStatus(statusRequest);
        existAnime.setType(typeRequest);
        existAnime.setDuration(durationRequest);
        existAnime.setMalScore(malScoreRequest);
        existAnime.setSeason(seasonRequest);
        existAnime.setYear(yearRequest);
        existAnime.setEpisodes(episodesRequest);
        existAnime.setSeries(series);
        existAnime.setGenres(genres);
        existAnime.setStudios(studios);
        existAnime.setProducers(producers);
        existAnime.setAired(airedRequest);
        existAnime.setAlternativeTitles(alternativeTitles);
        return animeMapper.animeToAnimeDtoLazy(animeRepository.save(existAnime));
    }



    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAnimeById(Long id){
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

        animeMapper.animeToAnimeDtoLazy(anime);
    }
}

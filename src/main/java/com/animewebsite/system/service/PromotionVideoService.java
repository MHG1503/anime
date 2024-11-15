package com.animewebsite.system.service;

import com.animewebsite.system.dto.req.EpisodeRequest;
import com.animewebsite.system.dto.req.PromotionVideoRequest;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.integration.MinioChannel;
import com.animewebsite.system.model.*;
import com.animewebsite.system.repository.AnimeRepository;
import com.animewebsite.system.repository.PromotionVideoRepository;
import com.animewebsite.system.repository.VideoRepository;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import io.minio.MinioClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PromotionVideoService {
    private final PromotionVideoRepository promotionVideoRepository;
    private final AnimeRepository animeRepository;
    private final VideoRepository videoRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PaginatedResponse<PromotionVideo> getAllPromotionVideos(int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);

        Page<PromotionVideo> promotionVideos = promotionVideoRepository.findAll(pageable);

        return new PaginatedResponse<>(
                promotionVideos.getContent(),
                promotionVideos.getTotalPages(),
                promotionVideos.getNumber(),
                promotionVideos.getTotalElements()
        );
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PromotionVideo getPromotionVideoById(Long id){
        return promotionVideoRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Khong tim thay promotion video nay"));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PromotionVideo createAndAddPromotionVideo(PromotionVideoRequest request){
        Long animeId = request.getAnimeId();
        Anime anime = animeRepository
                .findById(animeId)
                .orElseThrow(()-> new RuntimeException("Khong tim thay anime id: " + animeId));

        Video video = videoRepository.findByAnimeId(anime.getId(), NamedEntityGraph.fetching("video-with-promo"))
                .orElse(
                        Video
                        .builder()
                        .id(null)
                        .anime(anime)
                        .promo(new ArrayList<>())
                        .build()
                );
        if(video.getId() == null){
            video = videoRepository.save(video);
        }
        // https://youtu.be/{youtubeId}
        String youtubeId = request.getTrailerUrl().substring(17);
        Trailer trailer = Trailer
                .builder()
                .videoUrl(request.getTrailerUrl())
                .youtubeId(youtubeId)
                .image(Image
                        .builder()
                        .imageUrl("https://img.youtube.com/vi/" + youtubeId + "/default.jpg")
                        .smallImageUrl("https://img.youtube.com/vi/" + youtubeId + "/sddefault.jpg")
                        .mediumImageUrl("https://img.youtube.com/vi/" + youtubeId + "/mqdefault.jpg")
                        .largeImageUrl("https://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg")
                        .maximumImageUrl("https://img.youtube.com/vi/" + youtubeId + "/maxresdefault.jpg")
                        .publicId(null)
                        .build()
                )
                .build();

        PromotionVideo promotionVideo = PromotionVideo
                .builder()
                .title(request.getTitle())
                .trailer(trailer)
                .build();

        video.getPromo().add(promotionVideo);

        Video saveVideo = videoRepository.save(video);

        return saveVideo.getPromo()
                .stream()
                .filter(pv -> pv.getTitle().equals(request.getTitle()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Them promotion video that bai!"));
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PromotionVideo updatePromotionVideo(Long id,PromotionVideoRequest request){
        Long animeId = request.getAnimeId();
        Anime anime = animeRepository
                .findById(animeId)
                .orElseThrow(()-> new RuntimeException("Khong tim thay anime id: " + animeId));

        PromotionVideo promotionVideo = promotionVideoRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Khong tim thay promotion video id: " + id));

        // https://youtu.be/{youtubeId}
        String youtubeId = request.getTrailerUrl().substring(0,17);

        promotionVideo.getTrailer().setVideoUrl(request.getTrailerUrl());

        promotionVideo.getTrailer().setYoutubeId(youtubeId);

        promotionVideo.getTrailer().setImage
                (       Image
                        .builder()
                        .imageUrl("https://img.youtube.com/vi/" + youtubeId + "/default.jpg")
                        .smallImageUrl("https://img.youtube.com/vi/" + youtubeId + "/sddefault.jpg")
                        .mediumImageUrl("https://img.youtube.com/vi/" + youtubeId + "/mqdefault.jpg")
                        .largeImageUrl("https://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg")
                        .maximumImageUrl("https://img.youtube.com/vi/" + youtubeId + "/maxresdefault.jpg")
                        .publicId(null)
                        .build()
                );

        return promotionVideoRepository.save(promotionVideo);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePromotionVideo(Long id){
        promotionVideoRepository.deleteById(id);
    }
}

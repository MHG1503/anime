package com.animewebsite.system.service;

import com.animewebsite.system.dto.req.PromotionVideoRequest;
import com.animewebsite.system.dto.res.PaginatedResponse;
import com.animewebsite.system.model.*;
import com.animewebsite.system.repository.AnimeRepository;
import com.animewebsite.system.repository.PromotionVideoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PromotionVideoService {
    private final PromotionVideoRepository promotionVideoRepository;
    private final AnimeRepository animeRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PaginatedResponse<PromotionVideo> getAllPromotionVideos(int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize);

        Page<PromotionVideo> promotionVideos =  promotionVideoRepository.findAll(pageable);

        return new PaginatedResponse<>(
                promotionVideos.getContent(),
                promotionVideos.getTotalPages(),
                promotionVideos.getNumber() + 1,
                promotionVideos.getTotalElements()
        );
    }
    @Transactional
    public PaginatedResponse<PromotionVideo> getAllPromotionVideosByAnimeId(Long id,Integer pageNum, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum - 1,pageSize);

        Page<PromotionVideo> promotionVideos = promotionVideoRepository.findAllByAnimeId(id,pageable);

        return new PaginatedResponse<>(
                promotionVideos.getContent(),
                promotionVideos.getTotalPages(),
                promotionVideos.getNumber() + 1,
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

        // https://youtu.be/{youtubeId}
        String youtubeId = request.getTrailerUrl().substring(request.getTrailerUrl().length() - 11);
        Trailer trailer = Trailer
                .builder()
                .videoUrl(request.getTrailerUrl())
                .youtubeId(youtubeId)
                .image(Image
                        .builder()
                        .altTitle(null)
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
                .anime(anime)
                .build();

        return promotionVideoRepository.save(promotionVideo);
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
        String youtubeId = request.getTrailerUrl().substring(request.getTrailerUrl().length() - 11);
        promotionVideo.setTitle(request.getTitle());
        promotionVideo.getTrailer().setVideoUrl(request.getTrailerUrl());

        promotionVideo.getTrailer().setYoutubeId(youtubeId);

        promotionVideo.getTrailer().getImage().setImageUrl("https://img.youtube.com/vi/" + youtubeId + "/default.jpg");
        promotionVideo.getTrailer().getImage().setSmallImageUrl("https://img.youtube.com/vi/" + youtubeId + "/sddefault.jpg");
        promotionVideo.getTrailer().getImage().setMediumImageUrl("https://img.youtube.com/vi/" + youtubeId + "/mqdefault.jpg");
        promotionVideo.getTrailer().getImage().setLargeImageUrl("https://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg");
        promotionVideo.getTrailer().getImage().setMaximumImageUrl("https://img.youtube.com/vi/" + youtubeId + "/maxresdefault.jpg");
        promotionVideo.getTrailer().getImage().setPublicId(null);
        promotionVideo.getTrailer().getImage().setAltTitle(null);

        return promotionVideoRepository.save(promotionVideo);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePromotionVideo(Long id){
        promotionVideoRepository.deleteById(id);
    }
}

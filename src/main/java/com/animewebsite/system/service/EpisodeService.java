package com.animewebsite.system.service;

import com.animewebsite.system.convert.EpisodeMapper;
import com.animewebsite.system.dto.req.EpisodeRequest;
import com.animewebsite.system.dto.res.lazy.EpisodeDtoLazy;
import com.animewebsite.system.integration.MinioChannel;
import com.animewebsite.system.model.Anime;
import com.animewebsite.system.model.Episode;
import com.animewebsite.system.model.Image;
import com.animewebsite.system.model.Video;
import com.animewebsite.system.repository.AnimeRepository;
import com.animewebsite.system.repository.EpisodeRepository;
import com.animewebsite.system.repository.VideoRepository;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final AnimeRepository animeRepository;
    private final MinioChannel minioChannel;
    private final VideoRepository videoRepository;
    private final EpisodeMapper episodeMapper;
    private final String BUCKET = "anime";


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public EpisodeDtoLazy createAndAddEpisode(EpisodeRequest request, MultipartFile file, MultipartFile imageFile){

        Long animeId = request.getAnimeId();
        Anime anime = animeRepository
                .findById(animeId)
                .orElseThrow(()-> new RuntimeException("Khong tim thay anime id: " + animeId));

        Video video = videoRepository
                .findByAnimeId(anime.getId(), NamedEntityGraph.fetching("video-with-episodes"))
                .orElse(
                        Video
                                .builder()
                                .id(null)
                                .anime(anime)
                                .episodes(new ArrayList<>())
                                .build()
                );
        if(video.getId() == null){
            video = videoRepository.save(video);
        }

        String titleRequest = request.getTitle();
        String episodeRequest =request.getEpisode();
        String folder = anime.getName() + "/" + "Episode-" + episodeRequest;

        try{
            Image image = null;
            if( imageFile != null && !imageFile.isEmpty()){
                image = Image
                        .builder()
                        .publicId(null)
                        .imageUrl(minioChannel.upload(BUCKET, folder, imageFile))
                        .build();
            }

            Episode episodeVideo = Episode
                    .builder()
                    .title(titleRequest)
                    .episode(episodeRequest)
                    .anime(anime)
                    .image(image)
                    .videoUrl(file.isEmpty() ? null : minioChannel.upload(BUCKET,folder,file))
                    .build();

            if(video.getEpisodes().contains(episodeVideo)){
                throw new RuntimeException("Da ton tai episode nay roi!");
            }

            video.getEpisodes().add(episodeVideo);
            Video saveVideo = videoRepository.save(video);

            return episodeMapper.episodeToEpisodeDtoLazy(saveVideo.getEpisodes()
                    .stream()
                    .filter(e -> e.getTitle().equals(request.getTitle()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Them promotion video that bai!")));
        }catch (Exception e){
            minioChannel.delete(BUCKET, folder);
            throw new RuntimeException("Them moi that bai!");
        }
    }

    //TODO xu ly phan file va imageFile neu nhu updated that bai
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public EpisodeDtoLazy updateEpisode(Long id, EpisodeRequest request, MultipartFile file, MultipartFile imageFile){
        Long animeId = request.getAnimeId();
        String titleRequest = request.getTitle();
        String episodeRequest =request.getEpisode();

        Anime anime = animeRepository
                .findById(animeId)
                .orElseThrow(()-> new RuntimeException("Khong tim thay anime id: " + animeId));

        Video video = videoRepository
                .findByAnimeId(animeId, NamedEntityGraph.fetching("video-with-episodes"))
                .orElseThrow(()-> new RuntimeException("Khong tim thay videos tuong ung"));

        Episode episode = episodeRepository
                .findByIdAndAnimeId(id,anime.getId())
                .orElseThrow(()-> new RuntimeException("Khong tim thay episode : " + id));

        String newFileUrl;
        String newImageUrl;
        String folder = anime.getName() + "/" + "Episode-" + episodeRequest;
        String tempFolder = anime.getName() + "/" + "Episode-" + episodeRequest + "/temp";

        try {
            if(file != null && !file.isEmpty()){
                newFileUrl = minioChannel.upload(BUCKET,tempFolder,file);
                episode.setVideoUrl(newFileUrl);
            }

            episode.setTitle(titleRequest);
            episode.setEpisode(episodeRequest);

            if(imageFile != null && !imageFile.isEmpty()){
                newImageUrl = minioChannel.upload(BUCKET, tempFolder , imageFile);
                episode
                        .getImage()
                        .setImageUrl(newImageUrl);
            }

            Episode saveEpisode = episodeRepository.save(episode);

//            minioChannel.copyAndDelete();
            return episodeMapper.episodeToEpisodeDtoLazy(saveEpisode);
        }catch (Exception e){
            minioChannel.delete(BUCKET, tempFolder);
            throw new RuntimeException("Cap nhat episode that bai");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEpisode(Long id){
        Episode episode = episodeRepository
                .findById(id,NamedEntityGraph.fetching("episode-with-anime"))
                .orElseThrow(()->new RuntimeException("Khong tim thay episode id" + id));

        String folder = episode.getAnime().getName() + "/" + "Episode-" + episode.getEpisode();
        episodeRepository.delete(episode);
        minioChannel.delete(BUCKET, folder);
    }
}

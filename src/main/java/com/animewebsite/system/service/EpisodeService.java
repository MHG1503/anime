package com.animewebsite.system.service;

import com.animewebsite.system.convert.EpisodeMapper;
import com.animewebsite.system.dto.req.EpisodeRequest;
import com.animewebsite.system.dto.res.lazy.EpisodeDtoLazy;
import com.animewebsite.system.integration.MinioChannel;
import com.animewebsite.system.model.Anime;
import com.animewebsite.system.model.Episode;
import com.animewebsite.system.model.Image;
import com.animewebsite.system.repository.AnimeRepository;
import com.animewebsite.system.repository.EpisodeRepository;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final AnimeRepository animeRepository;
    private final MinioChannel minioChannel;
    private final EpisodeMapper episodeMapper;
    private final String BUCKET = "anime";


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public EpisodeDtoLazy createAndAddEpisode(EpisodeRequest request, MultipartFile file, MultipartFile imageFile){

        Long animeId = request.getAnimeId();
        Anime anime = animeRepository
                .findById(animeId)
                .orElseThrow(()-> new RuntimeException("Khong tim thay anime id: " + animeId));

        int posDot = request.getTitle().lastIndexOf(".");

        String titleRequest = request.getTitle().substring(0,posDot);
        Integer episodeRequest =request.getEpisode();
        String folder = anime.getName() + "/" + "Episode-" + episodeRequest;

        Optional<Episode> episode = episodeRepository
                .findByTitleAndEpisode(titleRequest,episodeRequest);

        if(episode.isPresent()){
            throw new RuntimeException("Da ton tai episode " + episode.get().getEpisode()+ " | title: " + episode.get().getTitle());
        }

        try{
            Image image = null;
            if( imageFile != null && !imageFile.isEmpty()){
                image = Image
                        .builder()
                        .publicId(null)
                        .imageUrl(minioChannel.upload(BUCKET, folder, imageFile))
                        .altTitle(imageFile.getOriginalFilename())
                        .build();
            }

            anime.setEpisodes(anime.getEpisodes() + 1);
            Episode episodeVideo = Episode
                    .builder()
                    .title(titleRequest)
                    .episode(episodeRequest)
                    .anime(anime)
                    .image(image)
                    .videoUrl((file != null && !file.isEmpty()) ? minioChannel.upload(BUCKET,folder,file) : null)
                    .build();

            return episodeMapper.episodeToEpisodeDtoLazy(episodeRepository.save(episodeVideo));
        }catch (Exception e){
            minioChannel.delete(BUCKET, folder);
            throw new RuntimeException("Them moi that bai!");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public EpisodeDtoLazy updateEpisode(Long id, EpisodeRequest request, MultipartFile file, MultipartFile imageFile){
        List<String> rasterImageFormats = List.of("jpg", "jpeg", "png", "gif", "bmp", "tif", "tiff", "webp");

        Long animeId = request.getAnimeId();
        String titleRequest = request.getTitle();
        Integer episodeRequest =request.getEpisode();
        String newFileName = "";
        String newImageUrl = "";


        Episode episode = episodeRepository
                .findByIdAndAnimeIdAndEpisode(id,animeId,request.getEpisode(),NamedEntityGraph.fetching("episode-with-anime"))
                .orElseThrow(()-> new RuntimeException("Khong tim thay episode : " + id));

        String folder = episode.getAnime().getName() + "/" + "Episode-" + episodeRequest;

        // before update
        List<String> getExistFileInFolder = minioChannel.getExistFileNameInFolder(BUCKET, folder);
        try {
            if(file != null && !file.isEmpty()){
                if(!minioChannel.doesFileExist(BUCKET, folder + "/" + file.getOriginalFilename())){
                    episode.setVideoUrl(minioChannel.upload(BUCKET,folder,file));
                    newFileName = file.getOriginalFilename();
                }else{
                    getExistFileInFolder.removeIf(name -> name.contains(folder + "/" + episode.getTitle()));
                }
            }


            episode.setTitle(titleRequest);
            episode.setEpisode(episodeRequest);

            if(imageFile != null && !imageFile.isEmpty()){
                if(!minioChannel.doesFileExist(BUCKET, folder + "/" + imageFile.getOriginalFilename())){
                    if(episode.getImage() != null){
                        episode
                                .getImage()
                                .setImageUrl(minioChannel.upload(BUCKET, folder , imageFile));

                        episode
                                .getImage()
                                .setAltTitle(imageFile.getOriginalFilename());
                    }else{
                        episode.setImage(Image
                                .builder()
                                        .imageUrl(minioChannel.upload(BUCKET, folder , imageFile))
                                        .altTitle(imageFile.getOriginalFilename())
                                .build());
                    }

                    newImageUrl = imageFile.getOriginalFilename();
                }else {
                    getExistFileInFolder.removeIf(name -> rasterImageFormats.contains(name.substring(name.lastIndexOf(".") + 1).toLowerCase()));
                }
            }

            Episode saveEpisode = episodeRepository.save(episode);

            minioChannel.delete(BUCKET, folder, getExistFileInFolder);

            return episodeMapper.episodeToEpisodeDtoLazy(saveEpisode);

        }catch (Exception e){
            minioChannel.delete(BUCKET, folder,
                    List.of(newFileName != null ? newFileName : "",newImageUrl != null ? newImageUrl : ""));

            throw new RuntimeException("Cap nhat episode that bai");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEpisode(Long id, Long animeId){
        Episode episode = episodeRepository
                .findByIdAndAnimeId(id,animeId,NamedEntityGraph.fetching("episode-with-anime"))
                .orElseThrow(()->new RuntimeException("Khong tim thay episode id" + id));

        String folder = episode.getAnime().getName() + "/" + "Episode-" + episode.getEpisode();
        episodeRepository.delete(episode);
        minioChannel.delete(BUCKET, folder);
    }
}

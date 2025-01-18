package com.animewebsite.system.repository;

import com.animewebsite.system.model.Episode;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode,Long> {

    Optional<Episode> findByIdAndAnimeIdAndEpisode(Long id,Long animeId,Integer episode,EntityGraph entityGraph);

    Optional<Episode> findByIdAndAnimeId(Long id,Long animeId,EntityGraph entityGraph);

    Optional<Episode> findByIdAndAnimeId(Long id,Long animeId);

    Optional<Episode> findByTitleAndEpisode(@Param("title") String title,@Param("episode") Integer episode);

    Page<Episode> findAllByAnimeId(Long id,Pageable pageable);

}

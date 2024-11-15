package com.animewebsite.system.repository;

import com.animewebsite.system.model.Anime;
import com.animewebsite.system.model.Video;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {

    Optional<Video> findByAnimeId(Long id, EntityGraph entityGraph);
}

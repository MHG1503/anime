package com.animewebsite.system.repository;

import com.animewebsite.system.model.Episode;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode,Long> {

    Optional<Episode> findByIdAndAnimeId(Long id,Long animeId);

    Optional<Episode> findById(Long id, EntityGraph entityGraph);
}

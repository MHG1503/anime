package com.animewebsite.system.repository;

import com.animewebsite.system.model.Series;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<Series,Long> {
    Optional<Series> findByTitle(String title,EntityGraph entityGraph);

    Optional<Series> findById(Long id, EntityGraph entityGraph);
}

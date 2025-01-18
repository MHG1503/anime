package com.animewebsite.system.repository;

import com.animewebsite.system.model.Anime;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime,Long> {
    @Query("SELECT a FROM Anime a JOIN FETCH a.alternativeTitles al WHERE a.name =:name OR al.alternativeName =:name")
    Optional<Anime> findByName(@Param("name") String name);

    Page<Anime> findBySeriesId(@Param("seriesId") Long seriesId, Pageable pageable);

    Page<Anime> findBySeriesIdIsNull(Pageable pageable);

    Optional<Anime> findById(Long id, EntityGraph entityGraph);

    @Query("SELECT a FROM Anime a JOIN a.genres g WHERE g.name IN :names")
    Page<Anime> findByGenresName(@Param("names") List<String> names, Pageable pageable);
}

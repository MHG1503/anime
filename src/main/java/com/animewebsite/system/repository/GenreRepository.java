package com.animewebsite.system.repository;

import com.animewebsite.system.model.Genre;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {
    Optional<Genre> findByName(String name);

    Optional<Genre> findById(Long id, EntityGraph entityGraph);
}

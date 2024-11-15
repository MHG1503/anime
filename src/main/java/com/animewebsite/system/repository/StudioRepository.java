package com.animewebsite.system.repository;

import com.animewebsite.system.model.Studio;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudioRepository extends JpaRepository<Studio,Long> {
    Optional<Studio> findByName(String name);

    Optional<Studio> findById(Long id, EntityGraph entityGraph);

    Page<Studio> findAll(Pageable pageable,EntityGraph entityGraph);

}

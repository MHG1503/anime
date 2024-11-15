package com.animewebsite.system.repository;

import com.animewebsite.system.model.Character;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character,Long> {
    Optional<Character> findByName(String name);

    Optional<Character> findById(Long id, EntityGraph entityGraph);
}

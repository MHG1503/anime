package com.animewebsite.system.repository;

import com.animewebsite.system.model.VoiceActor;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoiceActorRepository extends JpaRepository<VoiceActor,Long> {

    Optional<VoiceActor> findById(Long id, EntityGraph entityGraph);

    Optional<VoiceActor> findByName(String name);
}

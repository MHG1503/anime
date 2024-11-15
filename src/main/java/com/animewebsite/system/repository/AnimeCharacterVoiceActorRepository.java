package com.animewebsite.system.repository;

import com.animewebsite.system.model.AnimeCharacterVoiceActor;
import com.animewebsite.system.model.pk.AnimeCharacterVoiceActorId;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeCharacterVoiceActorRepository extends JpaRepository<AnimeCharacterVoiceActor, AnimeCharacterVoiceActorId> {

    Page<AnimeCharacterVoiceActor> findByAnimeCharacterVoiceActorIdCharacterId(Long characterId, Pageable pageable, EntityGraph entityGraph);

    @Modifying
    @Query("UPDATE AnimeCharacterVoiceActor acva SET acva.animeCharacterVoiceActorId.voiceActor.id = 9 WHERE acva.animeCharacterVoiceActorId.voiceActor.id = :id")
    void detachVoiceActorFromCharacters(@Param("id") Long id);

    void deleteByAnimeCharacterVoiceActorIdAnimeId(Long animeId);
}

package com.animewebsite.system.repository;

import com.animewebsite.system.model.AnimeCharacterVoiceActor;
import com.animewebsite.system.model.enums.CharacterRole;
import com.animewebsite.system.model.pk.AnimeCharacterVoiceActorId;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeCharacterVoiceActorRepository extends JpaRepository<AnimeCharacterVoiceActor, AnimeCharacterVoiceActorId> {

    Page<AnimeCharacterVoiceActor> findByAnimeCharacterVoiceActorIdCharacterId(Long characterId, Pageable pageable, EntityGraph entityGraph);

    @Modifying
    @Query("UPDATE AnimeCharacterVoiceActor acva SET acva.animeCharacterVoiceActorId.voiceActor.id = 1 WHERE acva.animeCharacterVoiceActorId.voiceActor.id = :id")
    void detachVoiceActorFromCharacters(@Param("id") Long id);

    void deleteByAnimeCharacterVoiceActorIdAnimeId(Long animeId);

    @Query("SELECT CASE WHEN (COUNT(acva) > 0) THEN true ELSE false END FROM AnimeCharacterVoiceActor acva " +
            "WHERE acva.animeCharacterVoiceActorId.character.id =:characterId " +
            "AND acva.animeCharacterVoiceActorId.anime.id =:animeId " +
            "AND acva.characterRole = :role")
    boolean existsByAnimeIdCharacterIdAndCharacterRole(@Param("animeId") Long animeId,
                                                       @Param("characterId") Long characterId ,
                                                       @Param("role") CharacterRole role);

    Page<AnimeCharacterVoiceActor> findByAnimeCharacterVoiceActorIdAnimeId(Long id,EntityGraph entityGraph, Pageable pageable);

    Page<AnimeCharacterVoiceActor> findByAnimeCharacterVoiceActorIdCharacterId(Long id,EntityGraph entityGraph, Pageable pageable);

    Page<AnimeCharacterVoiceActor> findByAnimeCharacterVoiceActorIdVoiceActorId(Long id,EntityGraph entityGraph, Pageable pageable);

}

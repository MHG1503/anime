package com.animewebsite.system.repository;

import com.animewebsite.system.model.AnimeCharacterVoiceActor;
import com.animewebsite.system.model.enums.CharacterRole;
import com.animewebsite.system.model.pk.AnimeCharacterVoiceActorId;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Modifying
    @Query("UPDATE AnimeCharacterVoiceActor acva SET acva.characterRole =:role " +
            "WHERE acva.animeCharacterVoiceActorId.anime.id =:animeId " +
            "AND acva.animeCharacterVoiceActorId.character.id =:characterId")
    void updateCharacterRoleByCharacterIdAndAnimeId(@Param("animeId")Long animeId,
                                                    @Param("characterId") Long characterId,
                                                    @Param("role") CharacterRole role);

    @Modifying
    @Transactional
    @Query("UPDATE AnimeCharacterVoiceActor acva SET " +
            "acva.animeCharacterVoiceActorId.character.id =:characterIdReq, " +
            "acva.animeCharacterVoiceActorId.voiceActor.id =:voiceActorIdReq " +
            "WHERE acva.animeCharacterVoiceActorId.anime.id =:animeId " +
            "AND acva.animeCharacterVoiceActorId.character.id =:characterId " +
            "AND acva.animeCharacterVoiceActorId.voiceActor.id =:voiceActorId"
    )
    void updateRelation(
                        @Param("animeId")Long animeId,
                        @Param("characterId") Long characterId,
                        @Param("voiceActorId") Long voiceActorId,
                        @Param("characterIdReq") Long characterIdReq,
                        @Param("voiceActorIdReq") Long voiceActorIdReq);

    Page<AnimeCharacterVoiceActor> findByAnimeCharacterVoiceActorIdAnimeId(Long id,EntityGraph entityGraph, Pageable pageable);

    Page<AnimeCharacterVoiceActor> findByAnimeCharacterVoiceActorIdCharacterId(Long id,EntityGraph entityGraph, Pageable pageable);

    Page<AnimeCharacterVoiceActor> findByAnimeCharacterVoiceActorIdVoiceActorId(Long id,EntityGraph entityGraph, Pageable pageable);

    List<AnimeCharacterVoiceActor> findByAnimeCharacterVoiceActorIdVoiceActorId(Long id,EntityGraph entityGraph);


    @Query( value = "SELECT c.id AS characterId, \n" +
            "           c.name AS characterName,\n" +
            "           cimg.image_url AS characterImage, \n" +
            "           acv.character_role AS role, \n" +
            "           JSON_ARRAYAGG(\n" +
            "               JSON_OBJECT(\n" +
            "                   'id', va.id, \n" +
            "                   'name', va.name,\n" +
            "                   'nationality', va.nationality,\n"+
            "                   'imageUrl', img.image_url\n" +
            "               )\n" +
            "           ) AS voiceActors\n" +
            "    FROM anime_character_voice_actor acv\n" +
            "    JOIN characters c ON acv.character_id = c.id\n" +
            "    JOIN voice_actors va ON acv.voice_actor_id = va.id\n" +
            "    JOIN images img ON va.image_id = img.id\n" +
            "    JOIN images cimg ON c.image_id = cimg.id\n" +
            "    WHERE acv.anime_id = :animeId\n" +
            "    GROUP BY c.id, c.name, acv.character_role", nativeQuery = true)
    Page<Object> getAllCharacterWithTheirVoiceActorByAnimeId(@Param("animeId")Long animeId, Pageable pageable);


}


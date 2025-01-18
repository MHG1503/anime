package com.animewebsite.system.repository;

import com.animewebsite.system.model.Character;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character,Long> {
    Optional<Character> findByName(String name);

    Optional<Character> findById(Long id, EntityGraph entityGraph);

    @Query(value = """
            SELECT
                                          c.id AS character_id,
                                          c.name AS character_name,
                                          c.about AS about,
                                          img.image_url as image_url,
                                          (SELECT JSON_ARRAYAGG(JSON_OBJECT('id', anime_distinct.id, 'name',anime_distinct.name,'type',anime_distinct.type ,'imageUrl', anime_distinct.image_url, 'characterRole',anime_distinct.character_role))
                                           FROM (
                                               SELECT DISTINCT a.id, a.name, a.type ,img.image_url, cav.character_role
                                               FROM animes a
                                               JOIN anime_character_voice_actor cav ON cav.anime_id = a.id
                                               JOIN images img ON img.id = a.image_id\s
                                               WHERE cav.character_id = c.id
                                           ) AS anime_distinct) AS anime_list,
                                          (SELECT JSON_ARRAYAGG(JSON_OBJECT('id', va_distinct.id, 'name', va_distinct.name,'nationality',va_distinct.nationality ,'imageUrl',va_distinct.image_url))
                                           FROM (
                                               SELECT DISTINCT va.id, va.name,va.nationality ,img.image_url
                                               FROM voice_actors va
                                               JOIN anime_character_voice_actor cav2 ON cav2.voice_actor_id = va.id
                                               JOIN images img ON img.id = va.image_id\s
                                               WHERE cav2.character_id = c.id
                                           ) AS va_distinct) AS voice_actor_list
                                      FROM characters c JOIN images img ON c.image_id = img.id
                                      WHERE c.id = :characterId
            """,
            nativeQuery = true)
    Optional<Object> findCharacterWithAnimeAndVoiceActors(@Param("characterId") Long characterId);
}

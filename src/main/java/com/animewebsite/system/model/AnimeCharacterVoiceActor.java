package com.animewebsite.system.model;

import com.animewebsite.system.model.enums.CharacterRole;
import com.animewebsite.system.model.pk.AnimeCharacterVoiceActorId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "anime_character_voice_actor")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "anime-character-voice_actor",
        attributeNodes = {
                @NamedAttributeNode("animeCharacterVoiceActorId"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "character",
                        attributeNodes =@NamedAttributeNode("character")
                ),
                @NamedSubgraph(
                        name = "anime",
                        attributeNodes =@NamedAttributeNode("anime")
                ),
                @NamedSubgraph(
                        name = "voiceActor",
                        attributeNodes =@NamedAttributeNode("voiceActor")
                )
        }),

})
public class AnimeCharacterVoiceActor  extends AbstractAuditBase{
    @EmbeddedId
    private AnimeCharacterVoiceActorId animeCharacterVoiceActorId;

    @Enumerated(EnumType.STRING)
    private CharacterRole characterRole;
}

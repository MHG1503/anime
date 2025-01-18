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
                @NamedAttributeNode(value = "animeCharacterVoiceActorId",subgraph = "animeCharacterVoiceActorId"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "animeCharacterVoiceActorId",
                        attributeNodes = {
                                @NamedAttributeNode(value = "character"),
                                @NamedAttributeNode(value = "anime"),
                                @NamedAttributeNode(value = "voiceActor")
                        }
                )
        }),

})
public class AnimeCharacterVoiceActor  extends AbstractAuditBase{
    @EmbeddedId
    private AnimeCharacterVoiceActorId animeCharacterVoiceActorId;

    @Enumerated(EnumType.STRING)
    private CharacterRole characterRole;


}

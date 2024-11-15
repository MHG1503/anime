package com.animewebsite.system.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "characters")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "character-with-anime-and-voice_actor",
                attributeNodes = {
                        @NamedAttributeNode("animeCharacterVoiceActors")
                })
})
public class Character extends AbstractAuditBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(length = 5000)
    private String about;

    @OneToMany(mappedBy = "animeCharacterVoiceActorId.character",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<AnimeCharacterVoiceActor> animeCharacterVoiceActors = new HashSet<>();
}

package com.animewebsite.system.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "voice_actors")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "voice_actor-with-image-anime-and-character",
                attributeNodes = {
                        @NamedAttributeNode("animeCharacterVoiceActors"),
                        @NamedAttributeNode("image")
                })
})
public class VoiceActor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate dob; // ngay sinh

    private String about; // gioi thieu ve nguoi long tieng

    private String url; // url dan den trang MAL

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;

    private String nationality;

    @OneToMany(mappedBy = "animeCharacterVoiceActorId.voiceActor",fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.DETACH})
    private Set<AnimeCharacterVoiceActor> animeCharacterVoiceActors = new HashSet<>();

}

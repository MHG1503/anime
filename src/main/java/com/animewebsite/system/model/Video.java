package com.animewebsite.system.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "videos")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "video-with-promo",
        attributeNodes = {
                @NamedAttributeNode("promo")
        }),
        @NamedEntityGraph(name = "video-with-episodes",
        attributeNodes = {
                @NamedAttributeNode("episodes")
        }),
        @NamedEntityGraph(name = "video-promo-and-episodes",
        attributeNodes = {
                @NamedAttributeNode("promo"),
                @NamedAttributeNode("episodes")
        })
})
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private List<PromotionVideo> promo;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private List<Episode> episodes;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.PERSIST,CascadeType.DETACH})
    @JoinColumn(name = "anime_id")
    private Anime anime;
}

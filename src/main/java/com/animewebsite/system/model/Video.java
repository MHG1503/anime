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
public class Video {
    @Id
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id")
    private List<PromotionVideo> promo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id")
    private List<Episode> episodes;

    @OneToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;
}

package com.animewebsite.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "episode")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "episode-with-anime",
        attributeNodes = {
                @NamedAttributeNode("anime")
        })
})
public class Episode extends AbstractAuditBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String episode;

    @Column(length = 2000)
    private String videoUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id")
    @JsonIgnore
    private Anime anime;

    public Episode(String title, String episode) {
        this.title = title;
        this.episode = episode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Episode episode1)) return false;
        return Objects.equals(title, episode1.title) && Objects.equals(episode, episode1.episode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, episode);
    }

}

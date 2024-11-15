package com.animewebsite.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "episode")
public class Episode extends AbstractAuditBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String episode;
    private String videoUrl;
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id")
    @JsonIgnore
    private Anime anime;
}

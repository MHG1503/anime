package com.animewebsite.system.model;

import com.animewebsite.system.model.enums.Season;
import com.animewebsite.system.model.enums.Status;
import com.animewebsite.system.model.enums.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "animes")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "anime-with-alternative_name-image-genre-producer-studio",
        attributeNodes = {
                @NamedAttributeNode("alternativeTitles"),
                @NamedAttributeNode("image"),
                @NamedAttributeNode("genres"),
                @NamedAttributeNode("producers"),
                @NamedAttributeNode("studios")
        })
})
public class Anime extends AbstractAuditBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinColumn(name = "anime_id")
    private Set<AlternativeTitle> alternativeTitles; // Ten dong nghia

    @Column(length = 2000)
    @Nullable
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Embedded
    private Aired aired; // ngay phat song

    private Double duration; // thoi luong moi tap

    private Double malScore; // diem dua tren trang web MyAnimeList

    @Enumerated(EnumType.STRING)
    private Season season;
    
    private int year;

    @Nullable
    private int episodes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "animes_genres",
            joinColumns = @JoinColumn(name = "anime_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "animes_producers",
            joinColumns = @JoinColumn(name = "anime_id"),
            inverseJoinColumns = @JoinColumn(name = "producer_id"))
    private Set<Producer> producers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "animes_studios",
            joinColumns = @JoinColumn(name = "anime_id"),
            inverseJoinColumns = @JoinColumn(name = "studio_id"))
    private Set<Studio> studios;

}

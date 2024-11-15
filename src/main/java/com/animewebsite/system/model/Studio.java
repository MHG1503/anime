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
@Table(name = "studios")
@NamedEntityGraphs({
        //todo: remove this
        @NamedEntityGraph(name = "studio-with-anime-and-image",
        attributeNodes = {
                @NamedAttributeNode("image"),
                @NamedAttributeNode("animeSet")
        }),
        //todo: remove this
        @NamedEntityGraph(name = "studio-with-anime",
        attributeNodes = {
                @NamedAttributeNode("animeSet")
        }),
        @NamedEntityGraph(name = "studio-with-image",
        attributeNodes = {
                @NamedAttributeNode("image")
        })
})
public class Studio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    //todo: remove this
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "studios")
    private Set<Anime> animeSet = new HashSet<>();

    //todo: remove this
    @PreRemove
    private void removeAnimeAssociations(){
        for(var anime : animeSet){
            anime.getStudios().remove(this);
        }
    }
}

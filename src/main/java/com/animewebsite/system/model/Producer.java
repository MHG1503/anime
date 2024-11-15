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
@Table(name = "producers")
@NamedEntityGraphs({
        //todo: remove this
        @NamedEntityGraph(name = "producer-with-anime-and-image",
        attributeNodes = {
                @NamedAttributeNode("animeSet"),
                @NamedAttributeNode("image")
        }),
        //todo: remove this
        @NamedEntityGraph(name = "producer-with-anime",
        attributeNodes = {
                @NamedAttributeNode("animeSet")
        }),
        @NamedEntityGraph(name = "producer-with-image",
                attributeNodes = {
                        @NamedAttributeNode("image")
        })
})
public class Producer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    //todo: remove this
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "producers")
    private Set<Anime> animeSet = new HashSet<>();

    //todo: remove this
    @PreRemove
    private void removeAnimeAssociations(){
        for(var anime : animeSet){
            anime.getProducers().remove(this);
        }
    }
}

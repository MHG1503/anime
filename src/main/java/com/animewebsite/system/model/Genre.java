package com.animewebsite.system.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "genres")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "genre-anime",
                attributeNodes = {
                @NamedAttributeNode("animeSet")
        })
})
public class Genre extends AbstractAuditBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "genres")
    private Set<Anime> animeSet = new HashSet<>();

    @PreRemove
    private void removeAnimeAssociation(){
        for(var anime : animeSet){
            anime.getGenres().remove(this);
        }
    }
}

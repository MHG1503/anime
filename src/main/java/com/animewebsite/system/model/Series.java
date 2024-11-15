package com.animewebsite.system.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "series")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "series-anime",
                attributeNodes = {
                @NamedAttributeNode("animeSet")
        })
})
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "series",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Anime> animeSet;

    public boolean removeAnimeFromSeries(Anime anime){
        anime.setSeries(null);
        return animeSet.removeIf(a -> a.getId().equals(anime.getId()));
    }

    public boolean addAnimeToSeries(Anime anime){
        anime.setSeries(this);
        return animeSet.add(anime);
    }
}

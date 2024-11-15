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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Set<Anime> animeSet;

    public boolean removeAnimeFromSeries(Long animeId){
        return animeSet.removeIf(anime -> anime.getId().equals(animeId));
    }

    public boolean addAnimeToSeries(Anime anime){
        return animeSet.add(anime);
    }
}

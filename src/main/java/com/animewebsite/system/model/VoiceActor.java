package com.animewebsite.system.model;

import com.animewebsite.system.model.enums.Nationality;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "voice_actors")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "voice_actor-with-image",
                attributeNodes = {
                        @NamedAttributeNode("image")
                })
})
public class VoiceActor extends AbstractAuditBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate dob; // ngay sinh

    @Column(length = 2000)
    private String about; // gioi thieu ve nguoi long tieng

    private String url; // url dan den trang MAL

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;

    @Enumerated(EnumType.STRING)
    private Nationality nationality;

}

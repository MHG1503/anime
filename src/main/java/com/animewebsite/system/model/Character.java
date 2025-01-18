package com.animewebsite.system.model;

import jakarta.annotation.Nullable;
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
@Table(name = "characters")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "character-with-image",
                attributeNodes = {
                        @NamedAttributeNode("image")
                })
})
public class Character extends AbstractAuditBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(length = 5000)
    @Nullable
    private String about;
}

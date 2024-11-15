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
@Table(name = "trailer")
public class Trailer {

    @Id
    private String youtubeId;

    private String videoUrl;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

}

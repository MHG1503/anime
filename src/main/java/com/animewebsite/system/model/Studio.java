package com.animewebsite.system.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "studios")
public class Studio extends AbstractAuditBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Nullable
    private LocalDate established;

    @Column(name = "introduce",length = 10000)
    @Nullable
    private String introduce;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    @Nullable
    private Image image;

}

package com.animewebsite.system.model;


import com.animewebsite.system.model.enums.TitleType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "alternative_titles")
public class AlternativeTitle extends AbstractAuditBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TitleType language;

    private String alternativeName;
}

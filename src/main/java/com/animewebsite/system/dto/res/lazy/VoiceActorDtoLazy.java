package com.animewebsite.system.dto.res.lazy;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VoiceActorDtoLazy {
    private Long id;

    private String name;

    private LocalDate dob; // ngay sinh

    private ImageDtoLazy image;

    private String nationality;
}

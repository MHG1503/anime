package com.animewebsite.system.dto.res.lazy;

import com.animewebsite.system.dto.res.NationalityResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VoiceActorDtoLazy {
    private Long id;

    private String name;

    private LocalDate dob; // ngay sinh

    private String about;

    private String url;

    private ImageDtoLazy image;

    private NationalityResponse nationality;
}

package com.animewebsite.system.dto.res.detail;

import com.animewebsite.system.dto.res.lazy.ImageDtoLazy;
import com.animewebsite.system.dto.res.lazy.anime_character_voiceactor.AnimeCharacterDtoLazy;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class VoiceActorDtoDetail {
    private Long id;

    private String name;

    private LocalDate dob; // ngay sinh

    private String about; // gioi thieu ve nguoi long tieng

    private String url; // url dan den trang MAL

    private ImageDtoLazy image;

    private String nationality;

    private Set<AnimeCharacterDtoLazy> animeCharacterDtoLazies = new HashSet<>();
}

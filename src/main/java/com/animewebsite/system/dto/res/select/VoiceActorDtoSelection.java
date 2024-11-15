package com.animewebsite.system.dto.res.select;

import com.animewebsite.system.dto.res.NationalityResponse;
import com.animewebsite.system.dto.res.lazy.ImageDtoLazy;
import com.animewebsite.system.model.Image;
import com.animewebsite.system.model.enums.Nationality;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoiceActorDtoSelection {
    private Long id;

    private String name;

    private ImageDtoLazy image;
}

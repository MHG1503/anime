package com.animewebsite.system.dto.res.lazy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterDtoLazy {
    private Long id;

    private String name;

    private ImageDtoLazy image;

    private String about;
}

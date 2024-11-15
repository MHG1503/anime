package com.animewebsite.system.dto.res.lazy;

import com.animewebsite.system.model.enums.Season;
import com.animewebsite.system.model.enums.Status;
import com.animewebsite.system.model.enums.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimeDtoLazy {
    private Long id;

    private String name;

    private Status status;

    private Type type;

    private Season season;

    private int year;

    private int episodes;

    private ImageDtoLazy imageDtoLazy;
}

package com.animewebsite.system.dto.res.lazy;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudioDtoLazy {
    private Long id;

    private String name;

    private LocalDate established;

    private String introduce;

    private ImageDtoLazy image;

}

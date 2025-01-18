package com.animewebsite.system.dto.res.lazy;

import com.animewebsite.system.model.Image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EpisodeDtoLazy {
    private Long id;
    private String title;
    private Integer episode;
    private String videoUrl;
    private ImageDtoLazy image;

}

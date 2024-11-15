package com.animewebsite.system.dto.res.detail;

import com.animewebsite.system.dto.res.lazy.AnimeDtoLazy;
import com.animewebsite.system.dto.res.lazy.ImageDtoLazy;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class StudioDtoDetail {
    private Long id;

    private String name;

    private ImageDtoLazy image;

    private Set<AnimeDtoLazy> animeDtoLazySet = new HashSet<>();
}

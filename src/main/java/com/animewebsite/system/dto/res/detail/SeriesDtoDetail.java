package com.animewebsite.system.dto.res.detail;

import com.animewebsite.system.dto.res.lazy.AnimeDtoLazy;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SeriesDtoDetail {
    private Long id;

    private String title;

    private Set<AnimeDtoLazy> animeDtoLazySet = new HashSet<>();
}

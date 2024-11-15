package com.animewebsite.system.dto.res.lazy;

import com.animewebsite.system.model.enums.TitleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlternativeTitleDtoLazy {
    private TitleType titleType;

    private String title;
}

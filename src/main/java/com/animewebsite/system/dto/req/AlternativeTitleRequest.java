package com.animewebsite.system.dto.req;

import com.animewebsite.system.model.enums.TitleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlternativeTitleRequest {
    private Long id;
    private String alternativeName;
    private TitleType language;
}

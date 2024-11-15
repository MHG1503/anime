package com.animewebsite.system.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NationalityResponse {
    private String name;
    private String code;
}

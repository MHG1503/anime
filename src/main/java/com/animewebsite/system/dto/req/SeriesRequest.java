package com.animewebsite.system.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeriesRequest {
    @NotBlank
    private String title;
}

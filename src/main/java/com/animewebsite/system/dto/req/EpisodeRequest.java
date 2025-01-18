package com.animewebsite.system.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class EpisodeRequest {
    @NonNull
    private Long animeId;
    @NotBlank
    private String title;
    @Min(1)
    private Integer episode;
}

package com.animewebsite.system.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionVideoRequest {
    private Long animeId;
    private String title;
    private String trailerUrl;
}

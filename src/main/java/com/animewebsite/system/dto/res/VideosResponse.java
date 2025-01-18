package com.animewebsite.system.dto.res;

import com.animewebsite.system.model.Episode;
import com.animewebsite.system.model.PromotionVideo;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideosResponse {
    PaginatedResponse<Episode> episodes;
    PaginatedResponse<PromotionVideo> promtionVideos;
}

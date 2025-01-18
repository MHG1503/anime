package com.animewebsite.system.dto.res.view;

import com.animewebsite.system.dto.res.NationalityResponse;
import com.animewebsite.system.model.enums.Nationality;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceActorViewDto {
    private Long id;
    private String name;
    private String imageUrl;
    private Object nationality;
}

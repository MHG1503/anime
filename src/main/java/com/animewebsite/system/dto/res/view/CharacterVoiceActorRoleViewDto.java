package com.animewebsite.system.dto.res.view;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterVoiceActorRoleViewDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String role;
    private List<VoiceActorViewDto> voiceActors;
}

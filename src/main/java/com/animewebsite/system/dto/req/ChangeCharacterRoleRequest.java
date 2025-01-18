package com.animewebsite.system.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeCharacterRoleRequest {
    private Long animeId;
    private Long characterId;
    private String characterRole;
}

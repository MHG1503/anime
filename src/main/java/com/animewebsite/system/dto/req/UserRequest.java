package com.animewebsite.system.dto.req;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserRequest {
    private String username;
    private MultipartFile avatar;

}

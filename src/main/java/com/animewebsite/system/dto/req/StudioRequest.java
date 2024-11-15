package com.animewebsite.system.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class StudioRequest {
    @NotBlank
    private String name;

    private MultipartFile avatar;
}

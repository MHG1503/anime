package com.animewebsite.system.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProducerRequest {
    @NotBlank
    private String name;

    private String introduce;

    private String date;

    private MultipartFile avatar;
}

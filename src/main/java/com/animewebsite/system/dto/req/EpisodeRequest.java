package com.animewebsite.system.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class EpisodeRequest {
    @NonNull
    private Long animeId;
    @NotBlank
    private String title;
    @NotBlank
    private String episode;
}

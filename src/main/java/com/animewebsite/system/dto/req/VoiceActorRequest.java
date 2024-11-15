package com.animewebsite.system.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Getter
@Setter
public class VoiceActorRequest {

    @NotBlank
    private String name;

    @NonNull
    private String dob; // ngay sinh

    @NotBlank
    private String about; // gioi thieu ve nguoi long tieng

    @NotBlank
    private String url; // url dan den trang MAL

    private String nationality;

}

package com.animewebsite.security.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    @Size(min = 5)
    private String password;
}

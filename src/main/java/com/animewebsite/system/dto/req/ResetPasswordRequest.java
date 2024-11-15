package com.animewebsite.system.dto.req;

public record ResetPasswordRequest(String newPassword, String confirmPassword) {
}

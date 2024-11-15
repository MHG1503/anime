package com.animewebsite.system.dto.req;

public record ChangePasswordRequest(String oldPassword, String newPassword, String confirmPassword) {
}

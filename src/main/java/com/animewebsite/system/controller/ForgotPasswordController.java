package com.animewebsite.system.controller;

import com.animewebsite.system.dto.req.ResetPasswordRequest;
import com.animewebsite.system.service.ForgotPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/forgotPassword")
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/verifyEmail/{email}")
    public ResponseEntity<?> verifyEmail(@PathVariable("email") String email){
        String result = forgotPasswordService.verifyEmail(email);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<?> verifyOtp(@PathVariable("otp") Integer otp,
                                       @PathVariable("email") String email){
        String result = forgotPasswordService.verifyOtp(email,otp);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/resetPassword/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable("email") String email,
                                           @RequestBody @Valid ResetPasswordRequest request){
        String result = forgotPasswordService.resetPassword(request,email);
        return ResponseEntity.ok(result);
    }
}

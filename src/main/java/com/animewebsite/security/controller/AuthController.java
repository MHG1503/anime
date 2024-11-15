package com.animewebsite.security.controller;

import com.animewebsite.security.common.constants.SecurityConstants;
import com.animewebsite.security.dto.req.LoginRequest;
import com.animewebsite.security.dto.req.RegisterRequest;
import com.animewebsite.security.service.AuthService;
import com.animewebsite.system.dto.req.ResetPasswordRequest;
import com.animewebsite.system.model.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request, HttpServletRequest httpServletRequest)
            throws MessagingException, UnsupportedEncodingException {

        String result = authService.register(request,getSiteURL(httpServletRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body("Dang ky tai khoan thanh cong");
    }

    @GetMapping("/verifyRegister")
    public ResponseEntity<?> verifyRegister(@Param("code") String code){
        String result = authService.verifyRegisterEmail(code);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest){
        String json = authService.login(loginRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(SecurityConstants.TOKEN_HEADER, json);
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}

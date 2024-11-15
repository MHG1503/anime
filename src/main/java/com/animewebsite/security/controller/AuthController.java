package com.animewebsite.security.controller;

import com.animewebsite.security.common.constants.SecurityConstants;
import com.animewebsite.security.dto.req.LoginRequest;
import com.animewebsite.security.dto.req.RegisterRequest;
import com.animewebsite.security.service.AuthService;
import com.animewebsite.system.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request){
        User saveUser = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest){
        String json = authService.login(loginRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(SecurityConstants.TOKEN_HEADER, json);
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }
}

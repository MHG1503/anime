package com.animewebsite.security.service;

import com.animewebsite.security.common.utils.JwtTokenUtils;
import com.animewebsite.security.dto.req.LoginRequest;
import com.animewebsite.security.dto.req.RegisterRequest;
import com.animewebsite.system.model.Role;
import com.animewebsite.system.model.User;
import com.animewebsite.system.repository.RoleRepository;
import com.animewebsite.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User register(RegisterRequest request){
        Role role = roleRepository.findByName("USER").orElseThrow(()->new RuntimeException("Not found role"));

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        User user = null;
        if(userOptional.isEmpty()) {
             user = User
                    .builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .username(request.getUsername())
                    .imageUrl("default.png")
                    .roles(List.of(role))
                    .enabled(true)
                    .build();
            return userRepository.save(user);
        }
        throw new RuntimeException("Da ton tai");
    }

    public String login(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(),loginRequest.getPassword());
        Authentication responseAuthentication = authenticationManager.authenticate(authenticationToken);
        if(responseAuthentication != null && responseAuthentication.isAuthenticated()){
            return JwtTokenUtils.createToken((UserDetails) responseAuthentication.getPrincipal());
        }
        throw new RuntimeException("Login failed");
    }
}

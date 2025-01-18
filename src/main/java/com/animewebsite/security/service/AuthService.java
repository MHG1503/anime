package com.animewebsite.security.service;

import com.animewebsite.security.common.utils.JwtTokenUtils;
import com.animewebsite.security.dto.req.LoginRequest;
import com.animewebsite.security.dto.req.RegisterRequest;
import com.animewebsite.system.dto.MailBody;
import com.animewebsite.system.model.Role;
import com.animewebsite.system.model.User;
import com.animewebsite.system.repository.RoleRepository;
import com.animewebsite.system.repository.UserRepository;
import com.animewebsite.system.service.CloudinaryService;
import com.animewebsite.system.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Transactional
    public String register(RegisterRequest request, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        Role role = roleRepository.findByName("USER").orElseThrow(()->new RuntimeException("Not found role"));
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        User user = null;

        if(userOptional.isPresent()){
            user = userOptional.get();
            if(user.getUsername().equals(request.getUsername())){
                throw new RuntimeException("Da ton tai username");
            }

            throw new RuntimeException("Da ton tai email");
        }else{
            String randomCode = RandomStringUtils.random(64,true,true);
             user = User
                    .builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .username(request.getUsername())
                    .roles(List.of(role))
                     .verificationCode(randomCode)
                    .enabled(false)
                    .build();

            User saveUser = userRepository.save(user);

            emailService.sendVerificationEmailForRegister(saveUser,siteUrl);

            return "Vui long xac nhan dang ky tai khoan thanh cong qua email!";
        }
    }

    @Transactional
    public String verifyRegisterEmail(String code){
        User user = userRepository.findByVerificationCode(code)
                .orElseThrow(()->new RuntimeException("Verification code khong hop le"));

        user.setEnabled(true);
        userRepository.save(user);
        return "Xac nhan thanh cong! Bay gio ban co the dang nhap vao website cua chung toi!";
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

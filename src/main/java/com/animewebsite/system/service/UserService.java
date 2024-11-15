package com.animewebsite.system.service;

import com.animewebsite.system.model.User;
import com.animewebsite.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Not found email: " + email));
    }
}

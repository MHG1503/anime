package com.animewebsite.security.config;

import com.animewebsite.security.model.JwtUser;
import com.animewebsite.system.model.User;
import com.animewebsite.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimeUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("Not found username"));
        return new JwtUser(user);
    }
}

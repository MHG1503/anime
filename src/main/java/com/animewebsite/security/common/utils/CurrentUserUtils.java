package com.animewebsite.security.common.utils;

import com.animewebsite.system.model.User;
import com.animewebsite.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class CurrentUserUtils {

    private final UserService userService;

    public User getCurrentUser(){
        return userService.findUserByEmail(getCurrentUserName());
    }

    private String getCurrentUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() != null){
            return (String) authentication.getPrincipal();
        }
        return null;
    }
}

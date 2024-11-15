package com.animewebsite.system.controller;

import com.animewebsite.security.common.utils.CurrentUserUtils;
import com.animewebsite.system.dto.req.ChangePasswordRequest;
import com.animewebsite.system.dto.req.UserRequest;
import com.animewebsite.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CurrentUserUtils currentUserUtils;

    @GetMapping("/user-info")
    public ResponseEntity<?> getCurrentUserInfo(){
        return ResponseEntity.ok(userService.currentUser(currentUserUtils.getCurrentUserName()));
    }

    @GetMapping
    public ResponseEntity<?> getAllUser(@RequestParam(value = "pageNum",defaultValue = "1",required = false) Integer pageNum,
                                        @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize){
        return ResponseEntity.ok(userService.getAllUser(pageNum,pageSize));
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable("id") Long id, @RequestBody ChangePasswordRequest changePasswordRequest){
        return ResponseEntity.ok(userService.updateUserPassword(changePasswordRequest,id));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id ,@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.updateUserInfo(id,userRequest));
    }
}

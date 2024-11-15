package com.animewebsite.system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/testing")
    public String getDemoHellWorld(){
        return "Testing success";
    }
}

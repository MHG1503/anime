package com.animewebsite.system.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name","dr4map5r3",
                "api_key", "834322528472428",
                "api_secret", "CcHidNkjB-kf9wDcFgpdZTp5l8E",
                "secure",true
        ));
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}

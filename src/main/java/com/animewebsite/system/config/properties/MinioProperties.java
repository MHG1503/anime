package com.animewebsite.system.config.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(
        prefix = "integration.minio",
        ignoreInvalidFields = false
)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class MinioProperties {
    private String minioUrl;
    private String accessKey;
    private String secretKey;
}

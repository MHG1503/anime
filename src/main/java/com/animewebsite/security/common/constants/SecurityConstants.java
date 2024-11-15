package com.animewebsite.security.common.constants;

public class SecurityConstants {
    public static final String ROLE_CLAIMS = "role";

    /**
     * "rememberMe" bằng false thì thời gian hết hạn là 1 giờ
     */
    public static final long EXPIRATION = 60*60;

    /**
     * "rememberMe" bằng true thì thời gian hết hạn là 7 ngày
     */
    public static final long EXPIRATION_REMEMBER = 60 * 60 * 24 * 7L;

    public static final String JWT_SECRET_KEY = "C*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w";

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer";

    public static final String TOKEN_TYPE = "JWT";

    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/webjars/**",
            //knife4j
            "/doc.html",
    };

    public static final String[] SYSTEM_WHITELIST = {
            "api/auth/**","api/forgotPassword/**","test/**"
    };

    private SecurityConstants() {

    }
}

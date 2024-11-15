package com.animewebsite.security.config;

import com.animewebsite.security.common.constants.SecurityConstants;
import com.animewebsite.security.exception.JwtAccessDeniedHandler;
import com.animewebsite.security.exception.JwtAuthenticationEntryPoint;
import com.animewebsite.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import static java.util.Collections.singletonList;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors((cors)-> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((request)-> request
                        .requestMatchers(SecurityConstants.SWAGGER_WHITELIST).permitAll()
                        .requestMatchers(SecurityConstants.SYSTEM_WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ech->ech.accessDeniedHandler(new JwtAccessDeniedHandler()))
                .formLogin(Customizer.withDefaults())
                .httpBasic(ech->ech.authenticationEntryPoint(new JwtAuthenticationEntryPoint()));


        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
        corsConfiguration.setExposedHeaders(singletonList(SecurityConstants.TOKEN_HEADER));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return corsConfigurationSource;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AnimeUserDetailsService animeUserDetailsService,
            PasswordEncoder passwordEncoder){
        AnimeWebsiteAuthenticationProvider authenticationProvider = new AnimeWebsiteAuthenticationProvider(passwordEncoder,animeUserDetailsService);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }
}

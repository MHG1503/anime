package com.animewebsite.security.filter;

import com.animewebsite.security.common.constants.SecurityConstants;
import com.animewebsite.security.common.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if(token == null || !token.startsWith(SecurityConstants.TOKEN_PREFIX)){
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request,response);
            return;
        }
        String tokenValue = token.substring(6);
        String username = JwtTokenUtils.extractUsername(tokenValue);
        if(SecurityContextHolder.getContext().getAuthentication() == null && username != null){
            UsernamePasswordAuthenticationToken authenticationToken = null;
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if(userDetails != null) {
                if (JwtTokenUtils.isTokenValid(tokenValue)) {
                    authenticationToken = JwtTokenUtils.getAuthentication(tokenValue);

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().equals("api/auth/login");
    }
}

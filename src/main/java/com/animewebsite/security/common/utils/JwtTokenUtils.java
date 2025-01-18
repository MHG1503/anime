package com.animewebsite.security.common.utils;

import com.animewebsite.security.common.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
public class JwtTokenUtils {
    private static final byte[] API_SECRET_KEY = SecurityConstants.JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(API_SECRET_KEY);

    public static String createToken(UserDetails userDetails){
        long expiration = SecurityConstants.EXPIRATION_REMEMBER;
        final Date createDate = new Date();
        final Date expirationDate = new Date(createDate.getTime() + expiration*10000);

        return Jwts
                .builder()
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .claim(SecurityConstants.ROLE_CLAIMS,String.join(",",
                                userDetails
                                .getAuthorities()
                                .stream().map(GrantedAuthority::getAuthority).toList()))
                .setIssuer("Anime Website")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(createDate)
                .setExpiration(expirationDate)
                .compact();
    }

    public static boolean isTokenValid(String token){
//        final String username = extractUsername(token);
        return isTokenExpiration(token);
    }

    private static boolean isTokenExpiration(String token){
        Date expirationDate = extractExpirationDate(token);
        return expirationDate.after(new Date());
    }

    private static Date extractExpirationDate(String token){
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    public static String extractUsername(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    private static Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static List<GrantedAuthority> getAuthorities(Claims claims){
        String role = (String) claims.get(SecurityConstants.ROLE_CLAIMS);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(role);
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token){
        Claims claims = extractAllClaims(token);
        List<GrantedAuthority> authorities = getAuthorities(claims);
        String username = claims.getSubject();
        log.info(authorities.toString());
        return new UsernamePasswordAuthenticationToken(username,null,authorities
        );
    }
}

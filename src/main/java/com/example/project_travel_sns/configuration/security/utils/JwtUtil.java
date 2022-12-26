package com.example.project_travel_sns.configuration.security.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;

@Slf4j
public class JwtUtil {

    public static boolean isValidToken(String token, String key) {
        String userName = null;
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error(e.getMessage());
        } catch (MalformedJwtException e) {
            log.error(e.getMessage());
        } catch (SignatureException e) {
            log.error(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public static UsernamePasswordAuthenticationToken createAuthentication(String token, String key) {
        String userName = Jwts.parser().setSigningKey(key).parseClaimsJws(token)
                .getBody().get("userName", String.class);
        return new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER")));
    }

    public static String createToken(String userName, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}

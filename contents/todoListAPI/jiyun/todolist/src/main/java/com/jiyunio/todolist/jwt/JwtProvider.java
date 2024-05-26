package com.jiyunio.todolist.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Value("${spring.jwt.secret}")
    private String secretKey;
    private final CustomUserDetailsService userDetailsService;
    private final long tokenValidTime = 60 * 60 * 1000L;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public JwtDTO createToken(String userId) {
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(getSecretKey())
                .compact();

        return JwtDTO.builder()
                .userId(userId)
                .accessToken(accessToken)
                .build();
    }

    //인증 (Authentication) 객체 반환
    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserId(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJwt(accessToken).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("AUTH-TOKEN");
    }

    //token 유효기간 검사
    public boolean validationToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(accessToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

package com.todolist.todolist.security;

import com.todolist.todolist.config.JwtTokenFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secretKeyBase64;
    private final long tokenValidityInSeconds;
    private Key key;

    public JwtTokenProvider(@Value("${jwt.secret}")String secretKeyBase64,
                            @Value("${jwt.token-validity-in-seconds}")long tokenValidityInSeconds) {
        this.secretKeyBase64 = secretKeyBase64;
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKeyBase64);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // token값 생성 -> 헤더에 저장하기 위함
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(new Date(System.currentTimeMillis() + this.tokenValidityInSeconds))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        List<? extends SimpleGrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // SecretKey를 사용해 Token Parsing
    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    // 발급된 Token이 만료 시간이 지났는지 체크
    public boolean isExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }


    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
       try {
           Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
           return true;
       }catch (io.jsonwebtoken.security.SecurityException e){
           return false;
       }

    }

    // 토큰 복호화
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


}
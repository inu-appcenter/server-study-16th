package com.jiyunio.todolist.jwt;

import com.jiyunio.todolist.customError.CustomException;
import com.jiyunio.todolist.customError.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtProvider {
    @Value("${spring.jwt.secret}")
    private String secretKey;
    private final CustomUserDetailsService userDetailsService;
    private final long tokenValidTime = 60 * 60 * 1000L;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public JwtDTO createToken(Authentication authentication) {
        log.info("createToken 메소드 들어옴");
        Date now = new Date();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        System.out.println(authorities);
        String accessToken = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("auth", authorities)
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(getSecretKey())
                .compact();

        return JwtDTO.builder()
                .grantType("Bearer")
                .userId(authentication.getName())
                .accessToken(accessToken)
                .build();
    }

    //인증 (Authentication) 객체 반환
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.NOT_EXIST_MEMBER);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserId(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJwt(accessToken).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            return token.substring(7);
        }
        return null;
    }

    //token 유효기간 검사
    public boolean validationToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(accessToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.NOT_EXIST_MEMBER);
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJwt(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}

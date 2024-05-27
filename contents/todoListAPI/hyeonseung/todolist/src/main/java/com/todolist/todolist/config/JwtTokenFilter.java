package com.todolist.todolist.config;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.security.JwtTokenProvider;
import com.todolist.todolist.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰 전송 x -> 로그인 x
        if(authorizationHeader == null){
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer"로 시작 x -> 잘못된 토큰
        if(!authorizationHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.split(" ")[1];

        if(jwtTokenProvider.isExpired(token)){
            filterChain.doFilter(request, response);
            return;
        }

        String loginId = jwtTokenProvider.getLoginIdFromToken(token);

        if ( loginId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (jwtTokenProvider.validateToken(token,loginId)) {
                Member loginMember = memberService.throwFindbyLoginId(loginId);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        loginMember.getLoginId(), null, List.of(new SimpleGrantedAuthority(loginMember.getRole().name())));

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

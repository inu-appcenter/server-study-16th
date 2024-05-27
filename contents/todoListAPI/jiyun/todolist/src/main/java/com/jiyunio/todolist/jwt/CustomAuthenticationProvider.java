package com.jiyunio.todolist.jwt;


import com.jiyunio.todolist.customError.CustomException;
import com.jiyunio.todolist.customError.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = authentication.getName();
        String userPw = (String) authentication.getCredentials();

        UserDetails user = userDetailsService.loadUserByUsername(userId);
        if (user == null || !this.passwordEncoder.matches(userPw, user.getPassword())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.NOT_EXIST_MEMBER);
        }
        return new UsernamePasswordAuthenticationToken(userId, userPw);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationProvider.class.isAssignableFrom(authentication);
    }
}

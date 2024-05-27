package com.jiyunio.todolist.jwt;

import com.jiyunio.todolist.customError.CustomException;
import com.jiyunio.todolist.customError.ErrorCode;
import com.jiyunio.todolist.member.Member;
import com.jiyunio.todolist.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws CustomException {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.NOT_EXIST_MEMBER));
        return User.builder()
                .username(member.getUserId())
                .password(member.getUserPw())
                .authorities(member.getRole())
                .build();
    }
}

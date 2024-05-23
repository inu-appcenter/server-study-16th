package com.appcenter.practice.security;

import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId){
        return memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(()->new CustomException(StatusCode.MEMBER_NOT_EXIST));
    }
}

